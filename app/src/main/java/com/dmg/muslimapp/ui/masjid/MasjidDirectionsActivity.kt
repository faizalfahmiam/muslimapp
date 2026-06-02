package com.dmg.muslimapp.ui.masjid

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.widget.Toolbar
import android.util.Log
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.akexorcist.googledirection.DirectionCallback
import com.akexorcist.googledirection.GoogleDirection
import com.akexorcist.googledirection.constant.TransportMode
import com.akexorcist.googledirection.model.Direction
import com.akexorcist.googledirection.model.Route
import com.akexorcist.googledirection.util.DirectionConverter
import com.dmg.muslimapp.R
import com.dmg.muslimapp.data.network.model.masjid.MasjidList
import com.dmg.muslimapp.ui.base.BaseActivity
import com.dmg.muslimapp.ui.masjid.list.MasjidListFragment
import com.dmg.muslimapp.utils.CommonUtils
import com.dmg.muslimapp.utils.ToastMessage
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import io.nlopez.smartlocation.SmartLocation
import io.nlopez.smartlocation.location.providers.LocationGooglePlayServicesProvider
import rebus.permissionutils.PermissionEnum
import rebus.permissionutils.PermissionManager
import java.util.ArrayList

class MasjidDirectionsActivity : BaseActivity(), OnMapReadyCallback, DirectionCallback  {

    @BindView(R.id.toolbar)
    lateinit var mToolbar: Toolbar

    @BindView(R.id.tv_direction)
    lateinit var mTvDirection: TextView

    private var mMap: GoogleMap? = null
    private lateinit var mapView: MapView

    private lateinit var mClient: SettingsClient
    private lateinit var builder: LocationSettingsRequest.Builder
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private lateinit var mLocationRequest: LocationRequest
    private var marker: Marker? = null
    private var marker2: Marker? = null

    private var origin = LatLng(0.0, 0.0)
    private var destination = LatLng(0.0, 0.0)
    private val colors = arrayOf("#2699FB", "#63D6D8", "#bcd8f1")

    private var bottomSheetFragment: MasjidListFragment = MasjidListFragment()

    private lateinit var objData: MasjidList

    companion object {
        fun getStartIntent(context: Context): Intent {
            return Intent(context, MasjidDirectionsActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.masjid_screen_directions)
        ButterKnife.bind(this)

        mapView = findViewById<MapView>(R.id.mapview)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        setup()
    }

    fun setup() {
        setToolbar(mToolbar)
        showBackButton(true)

        objData = intent.getSerializableExtra("obj") as MasjidList
        mTvDirection.text = objData.nama_tempat

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
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, locationCallback, null)
        }.addOnFailureListener { e ->
            e.printStackTrace()

            if (e is ResolvableApiException) {
                try {
                    val resolvable = e as ResolvableApiException
                    resolvable.startResolutionForResult(this, LocationGooglePlayServicesProvider.REQUEST_CHECK_SETTINGS)
                } catch (ex: IntentSender.SendIntentException) {
                    Log.e("on failure ", ex.message)
                }
            }
        }
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            //hideLoading()
            if (locationResult == null) {
                Log.e("Map", "locationCallback null")
                return
            }

            Log.e("Map", "locationResult")

            for (location in locationResult.locations) {
                val latLng = LatLng(location.latitude, location.longitude)

                origin = latLng

                Log.e("Map", "latLng" + location.latitude + "," + location.longitude)

                marker?.position = latLng
                mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16f))

                SmartLocation.with(this@MasjidDirectionsActivity).geocoding()
                        .reverse(location) { location1, list ->

                        }
            }
            mFusedLocationClient.removeLocationUpdates(this)
            //dataManager.setLocationManual(false)
            //switch_location_auto.isChecked = true

            locationMasjidSelection()
        }
    }

    fun locationMasjidSelection() {
        destination = LatLng(objData.lat.toDouble(), objData.long.toDouble())
        marker2?.position = destination
        directions()
    }

    fun directions() {
        GoogleDirection.withServerKey(getString(R.string.map_api_key))
                .from(origin)
                .to(destination)
                .transportMode(TransportMode.DRIVING)
                .alternativeRoute(true)
                .execute(this);
    }

    override fun onDirectionFailure(t: Throwable?) {
        ToastMessage("Directions Failed", this)
    }

    override fun onDirectionSuccess(direction: Direction, rawBody: String) {
        //Snackbar.make(btnRequestDirection, "Success with status : " + direction.getStatus(), Snackbar.LENGTH_SHORT).show();

        //ToastMessage("Directions response", this)

        if (direction.isOK) {
            //ToastMessage("Directions OK", this)
            //mMap?.addMarker( MarkerOptions().position(origin));
            //mMap?.addMarker( MarkerOptions().position(destination));
            mMap?.addMarker(MarkerOptions()
                    .position(origin)
                    .icon(BitmapDescriptorFactory.fromBitmap(CommonUtils.resizeMapIcons(this,"ic_marker_origin_elipse_blue",45,45)))
                    .anchor(0.5f, 0.5f)
                    .title("Posisi anda"))
                    //.snippet("Alamat anda"))

            mMap?.addMarker(MarkerOptions()
                    .position(destination)
                    .icon(BitmapDescriptorFactory.fromBitmap(CommonUtils.resizeMapIcons(this,"ic_marker_masjid",45,65)))
                    .anchor(0.5f, 0.5f)
                    .title(objData.nama_tempat)
                    .snippet(objData.alamat))

            for (i in direction.getRouteList()!!.indices.reversed()) {
                val route : Route = direction.getRouteList().get(i)
                val directionPositionList : ArrayList<LatLng> = route.getLegList().get(0).getDirectionPoint();

                val color = colors[i % colors.size];
                mMap?.addPolyline(DirectionConverter.createPolyline(this, directionPositionList, 5, Color.parseColor(color)));
            }

            setCameraWithCoordinationBounds(direction.getRouteList().get(0));

        }else{
            //ToastMessage("Directions Not OK", this)
        }
    }

    fun setCameraWithCoordinationBounds(route: Route) {
        val southwest = route.bound.southwestCoordination.coordination;
        val northeast = route.bound.northeastCoordination.coordination;
        val bounds = LatLngBounds(southwest, northeast);
        mMap?.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
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

    @OnClick(R.id.img_refresh)
    fun onRefreshLocation(){
        requestLocation()
    }

    @OnClick(R.id.img_my_location)
    fun onMyLocation(){
        mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(origin, 16f))
    }

    @OnClick(R.id.btn_directions)
    fun onDirections() {
        val gmmIntentUri = Uri.parse("google.navigation:q=" + destination.latitude + "," + destination.longitude)
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")

        val browserUri = Uri.parse("http://maps.google.com/maps?saddr=" + origin.latitude + "," + origin.longitude +
                "&daddr=" + destination.latitude + "," + destination.longitude)

        try {
            startActivity(mapIntent)
        } catch (ex: ActivityNotFoundException) {
            try {
                val unrestrictedIntent = Intent(Intent.ACTION_VIEW, browserUri)
                startActivity(unrestrictedIntent)
            } catch (innerEx: ActivityNotFoundException) {
                showMessage("Please install a maps application")
            }

        }

    }

    fun hideBottomSheet() {
        if (bottomSheetFragment.isVisible) {
            bottomSheetFragment.dismiss()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        Log.e("Map", "onMapReady")

        //val latLng = LatLng(0.0, 0.0)
        //marker = mMap?.addMarker(MarkerOptions().position(latLng))
        //marker2 = mMap?.addMarker(MarkerOptions().position(latLng))
        //mMap?.moveCamera(CameraUpdateFactory.newLatLng(latLng))
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionManager.handleResult(this, requestCode, permissions, grantResults)
    }
}