package com.dmg.muslimapp.ui.setup.location

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Address
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.OnTextChanged
import com.dmg.muslimapp.R
import com.dmg.muslimapp.data.db.model.users.UserLocation
import com.dmg.muslimapp.ui.base.BaseFragment
import com.dmg.muslimapp.ui.main.MainActivity
import com.dmg.muslimapp.utils.CommonUtils
import com.dmg.muslimapp.utils.Constants
import com.dmg.muslimapp.utils.ToastMessage
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.location.places.GeoDataClient
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.Places
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import io.nlopez.smartlocation.SmartLocation
import rebus.permissionutils.PermissionEnum
import rebus.permissionutils.PermissionManager
import io.nlopez.smartlocation.location.providers.LocationGooglePlayServicesProvider.REQUEST_CHECK_SETTINGS
import kotlinx.android.synthetic.main.setup_location_fragment.*
import java.util.*


class LocationFragment : BaseFragment(), LocationFragmentView, OnMapReadyCallback {
    private lateinit var userLocation: UserLocation

    private lateinit var mLayoutManager: LinearLayoutManager

    private lateinit var searchLocationAdapter: SearchLocationAdapter

    private lateinit var mBottomSheetBehavior: BottomSheetBehavior<*>

    private lateinit var mPresenter: LocationFragmentPresenter

    private var mMap: GoogleMap? = null
    private lateinit var mapView: MapView
    private lateinit var mGeoDataClient: GeoDataClient

    private lateinit var mClient: SettingsClient
    private lateinit var builder: LocationSettingsRequest.Builder
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private lateinit var mLocationRequest: LocationRequest
    private var marker: Marker? = null

    private lateinit var selectedLocation: Location
    private lateinit var selectedAddress: Address

    private var manualLocation = false

    private var activityIsRunning = false

    companion object {
        val TAG = "LocationFragment"

        fun newInstance(): LocationFragment {
            val args = Bundle()
            val fragment = LocationFragment()
            fragment.setArguments(args)
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.setup_location_fragment, container, false)
        ButterKnife.bind(this, view)

        mapView = view.findViewById(R.id.mapview) as MapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        return view
    }

    override fun setUp(view: View) {
        mPresenter = LocationFragmentPresenter(this)
        //permissionChecking()
        mLayoutManager = LinearLayoutManager(this.context)
        mGeoDataClient = Places.getGeoDataClient(this.activity!!, null)
        mPresenter.setGeoDataClient(mGeoDataClient)
        searchLocationAdapter = SearchLocationAdapter(this.context!!, mPresenter, mGeoDataClient, LatLngBounds(LatLng(0.0, 0.0), LatLng(0.0, 0.0)), null)
        mLayoutManager.orientation = LinearLayoutManager.VERTICAL

        recycler_view.layoutManager = mLayoutManager
        recycler_view.adapter = searchLocationAdapter

        mBottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet)
        mBottomSheetBehavior.peekHeight = 200
        mBottomSheetBehavior.isHideable = false
        mBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this.activity!!)
        mLocationRequest = LocationRequest()
        mLocationRequest.interval = 10000
        mLocationRequest.fastestInterval = 5000
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        builder = LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest)
        mClient = LocationServices.getSettingsClient(this.activity!!)

        requestLocation()

        switch_location_auto.setOnCheckedChangeListener { view, isChecked ->
            if (isChecked) {
                requestLocation()
                //dataManager.setLocationManual(false)
                layout_search_location.visibility = View.GONE
                recycler_view.visibility = View.GONE
            } else {
                // searchLocationFragment.show(getSupportFragmentManager(), searchLocationFragment.getTag());
                layout_search_location.visibility = View.VISIBLE
                //dataManager.setLocationManual(true)
                recycler_view.visibility = View.VISIBLE
            }
        }
    }

    @OnTextChanged(R.id.search_location)
    fun onSearchLocation(text: CharSequence) {
        searchLocationAdapter.filter.filter(text)
        Log.e("Search","lokasi manual")
    }

    @OnClick(R.id.layout_back)
    fun onPrev() {
        activity?.onBackPressed();
    }

    @OnClick(R.id.btn_confirm)
    fun onConfirm() {
        if(switch_location_auto.isChecked) {
            requestLocation()
        }else{
            if(!manualLocation) {
                ToastMessage("Silahkan cari lokasi anda.", this.activity!!)
                return
            }
        }
        mPresenter.setLocation(this.activity!!, userLocation)

    }

    private fun requestLocation() {
        if (ActivityCompat.checkSelfPermission(this.activity!!, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.activity!!, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            askPermission()
            return
        }
        //showLoading()
        mClient.checkLocationSettings(builder.build()).addOnCompleteListener { task1 ->
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, locationCallback, null) }.addOnFailureListener { e ->
                e.printStackTrace()

                if (e is ResolvableApiException) {
                    try {
                        val resolvable = e as ResolvableApiException
                        resolvable.startResolutionForResult(this.activity!!, REQUEST_CHECK_SETTINGS)
                    } catch (ex: IntentSender.SendIntentException) {
                        Log.e("on failure " , ex.message)
                    }
                }
            }
    }

    private fun askPermission() {
        PermissionManager.Builder()
                .permission(PermissionEnum.ACCESS_FINE_LOCATION)
                .askAgain(true)
                .askAgainCallback { response ->
                    Log.e("askAgainCallback", "askAgainCallBack " + response.toString())
                }
                .callback { permissionsGranted: ArrayList<PermissionEnum>, permissionsDenied: ArrayList<PermissionEnum>, permissionsDeniedForever: ArrayList<PermissionEnum>, permissionsAsked: ArrayList<PermissionEnum> ->
                    Log.e("permission callback", permissionsGranted.toString() + " " + permissionsDenied.toString() + " " + permissionsDeniedForever.toString() + " " + permissionsAsked.toString())
                    var permissionGranted = false

                    for (permissionEnum in permissionsGranted) {
                        if (permissionEnum.equals(PermissionEnum.ACCESS_FINE_LOCATION)) {
                            permissionGranted = true
                        }
                    }

                    if (permissionGranted) {
                        requestLocation()
                        Log.e("Callback", "true")
                    } else {
                        switch_location_auto.isChecked = false
                        Log.e("Callback", "false")
                    }
                }
                .ask(this);
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            //hideLoading()
            if (locationResult == null) {
                Log.e("Map","locationCallback null")
                return
            }

            Log.e("Map","locationResult")

            for (location in locationResult.locations) {
                val latLng = LatLng(location.latitude, location.longitude)

                Log.e("Map","latLng" + location.latitude + "," +location.longitude)

                marker?.position = latLng
                mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16f))

                SmartLocation.with(context).geocoding()
                        .reverse(location) { location1, list ->
                            if (list.size > 0 && activityIsRunning) {
                                val result = list[0]
                                val address = CommonUtils.getShortAddress(result)
                                //val address = result.getAddressLine(0)
                                selected_location.text = (address)
                                selectedLocation = location
                                selectedAddress = result

                                val latitude: Double? = result.latitude
                                val longitude: Double? = result.longitude
                                val addressLines: String? = result.getAddressLine(0)
                                val thoroughfare: String? = result.thoroughfare
                                val locality: String? = result.locality
                                val adminArea: String? = result.adminArea
                                val subAdminArea: String? = result.subAdminArea
                                val countryName: String? = result.countryName
                                val postalCode: String? = result.postalCode
                                val featureName: String? = result.featureName
                                val phone: String? = result.phone

                                userLocation = UserLocation(
                                        0,
                                        latitude ?: Constants.DEFAULT_BDG_LAT,
                                        longitude ?: Constants.DEFAULT_BDG_LNG,
                                        addressLines ?: "",
                                        thoroughfare ?: "",
                                        locality ?: "",
                                        adminArea ?: "",
                                        subAdminArea ?: "",
                                        countryName ?: "",
                                        postalCode ?: "",
                                        featureName ?: "")
                            }
                        }
            }
            mFusedLocationClient.removeLocationUpdates(this)
            //dataManager.setLocationManual(false)
            switch_location_auto.isChecked = true
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        Log.e("Map","onMapReady")

        val latLng = LatLng(0.0, 0.0)
        marker = mMap?.addMarker(MarkerOptions().position(latLng))
        mMap?.moveCamera(CameraUpdateFactory.newLatLng(latLng))
    }

    override fun onManualPlaceSeted(place: Place) {
        //hideKeyboard()
        selectedLocation = Location("")
        selected_location.text = place.address.toString()
        search_location.setText("")

        selectedLocation.latitude = place.latLng.latitude
        selectedLocation.longitude = place.latLng.longitude

        SmartLocation.with(this.context).geocoding()
                .reverse(selectedLocation) { location, list ->
                    if (list.size > 0 && activityIsRunning) {
                        val result = list[0]
                        val address = CommonUtils.getShortAddress(result)
                        //val address = result.getAddressLine(0)
                        selected_location.text = (address)
                        selectedLocation = location
                        selectedAddress = result

                        val latitude: Double? = result.latitude
                        val longitude: Double? = result.longitude
                        val addressLines: String? = result.getAddressLine(0)
                        val thoroughfare: String? = result.thoroughfare
                        val locality: String? = result.locality
                        val adminArea: String? = result.adminArea
                        val subAdminArea: String? = result.subAdminArea
                        val countryName: String? = result.countryName
                        val postalCode: String? = result.postalCode
                        val featureName: String? = result.featureName

                        userLocation = UserLocation(
                                0,
                                latitude ?: Constants.DEFAULT_BDG_LAT,
                                longitude ?: Constants.DEFAULT_BDG_LNG,
                                addressLines ?: "",
                                thoroughfare ?: "",
                                locality ?: "",
                                adminArea ?: "",
                                subAdminArea ?: "",
                                countryName ?: "",
                                postalCode ?: "",
                                featureName ?: "")

                        manualLocation = true
                    }
                }
        marker?.position = place.latLng
        mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(place.latLng, 16f))
    }

    override fun onStart() {
        super.onStart()
        activityIsRunning = true
    }

    override fun onStop() {
        super.onStop()
        activityIsRunning = false
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onDestroy() {
        mapView.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        mapView.onLowMemory()
        super.onLowMemory()
    }

    override fun onPause() {
        mapView.onPause()
        super.onPause()
    }

    override fun setLocationSuccess(){
        mPresenter.appConfig()
    }

    override fun setLocationFailed(){
        mPresenter.appConfig()
    }

    override fun configSuccess() {
        val intent = MainActivity.getStartIntent(this.activity!!)
        startActivity(intent)
        activity?.finish()
    }

    override fun configFailed() {
        showMessage("Configuration Failed")
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionManager.handleResult(this, requestCode, permissions, grantResults)
    }

    private fun showDialogOK(message: String, okListener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(this.activity!!)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show()
    }

    private fun explain(msg: String) {
        val dialog = android.support.v7.app.AlertDialog.Builder(this.activity!!)
        dialog.setMessage(msg)
                .setPositiveButton("Yes") { paramDialogInterface, paramInt ->
                    //  permissionsclass.requestPermission(type,code);
                    startActivity(Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:com.dmg.muslimapp")))
                }
                .setNegativeButton("Cancel") { paramDialogInterface, paramInt -> getActivity()?.finish() }
        dialog.show()
    }
}

