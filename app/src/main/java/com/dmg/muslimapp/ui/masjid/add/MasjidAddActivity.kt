package com.dmg.muslimapp.ui.masjid.add

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.widget.Toolbar
import android.util.Log
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.dmg.muslimapp.R
import com.dmg.muslimapp.ui.base.BaseActivity
import com.dmg.muslimapp.ui.masjid.dialog.MasjidAddDialog
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import io.nlopez.smartlocation.SmartLocation
import io.nlopez.smartlocation.location.providers.LocationGooglePlayServicesProvider
import rebus.permissionutils.PermissionEnum
import rebus.permissionutils.PermissionManager
import java.util.ArrayList

class MasjidAddActivity: BaseActivity(), OnMapReadyCallback {

    @BindView(R.id.toolbar)
    lateinit var mToolbar: Toolbar

    private var mMap: GoogleMap? = null
    private lateinit var mapView: MapView

    private lateinit var mClient: SettingsClient
    private lateinit var builder: LocationSettingsRequest.Builder
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private lateinit var mLocationRequest: LocationRequest
    private var marker: Marker? = null

    companion object {
        fun getStartIntent(context: Context): Intent {
            return Intent(context, MasjidAddActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.masjid_add)
        ButterKnife.bind(this)

        mapView = findViewById<MapView>(R.id.mapview)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        setup()
    }

    fun setup(){
        setToolbar(mToolbar)
        showBackButton(true)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mLocationRequest = LocationRequest()
        mLocationRequest.interval = 10000
        mLocationRequest.fastestInterval = 5000
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        builder = LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest)
        mClient = LocationServices.getSettingsClient(this)

        requestLocation()
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
                        //switch_location_auto.isChecked = false
                        Log.e("Callback", "false")
                    }
                }
                .ask(this);
    }

    private fun requestLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                    resolvable.startResolutionForResult(this, LocationGooglePlayServicesProvider.REQUEST_CHECK_SETTINGS)
                } catch (ex: IntentSender.SendIntentException) {
                    Log.e("on failure " , ex.message)
                }
            }
        }
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

                SmartLocation.with(this@MasjidAddActivity).geocoding()
                        .reverse(location) { location1, list ->
                            /*if (list.size > 0 && activityIsRunning) {
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
                            }*/
                        }
            }
            mFusedLocationClient.removeLocationUpdates(this)
            //dataManager.setLocationManual(false)
            //switch_location_auto.isChecked = true
        }
    }

    override fun onStart() {
        super.onStart()
        //activityIsRunning = true
    }

    override fun onStop() {
        super.onStop()
        //activityIsRunning = false
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

    /*@OnClick(R.id.layout_tap)
    fun onTap(){
        layout_tap.visibility = View.GONE
        tv_tap.visibility = View.GONE
        tv_menu.visibility = View.GONE
        tv_nearby.visibility = View.GONE
        tv_add.visibility = View.GONE
        tv_refresh.visibility = View.GONE
        tv_koordinat.visibility = View.GONE
    }

    @OnClick(R.id.img_menu)
    fun onMenu(){
        val intent = MasjidMenu.getStartIntent(this)
        startActivity(intent)
    }*/

    @OnClick(R.id.btn_submit)
    fun onSubmit(){
        MasjidAddDialog.newInstance().showAdd(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        Log.e("Map","onMapReady")

        val latLng = LatLng(0.0, 0.0)
        marker = mMap?.addMarker(MarkerOptions().position(latLng))
        mMap?.moveCamera(CameraUpdateFactory.newLatLng(latLng))
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionManager.handleResult(this, requestCode, permissions, grantResults)
    }
}