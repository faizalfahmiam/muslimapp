package com.dmg.muslimapp.ui.masjid.home

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.dmg.muslimapp.R
import com.dmg.muslimapp.data.network.model.masjid.MasjidList
import com.dmg.muslimapp.ui.base.BaseActivity
import com.dmg.muslimapp.ui.masjid.MasjidDirectionsActivity
import com.dmg.muslimapp.ui.masjid.MasjidMenu
import com.dmg.muslimapp.ui.masjid.add.MasjidAddActivity
import com.dmg.muslimapp.ui.masjid.detail.MasjidDetailActivity
import com.dmg.muslimapp.ui.masjid.list.MasjidListFragment
import com.dmg.muslimapp.ui.masjid.nearby.MasjidNearbyScreenActivity
import com.dmg.muslimapp.ui.masjid.setting.MasjidSettingActivity
import com.dmg.muslimapp.utils.CommonUtils
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import io.nlopez.smartlocation.SmartLocation
import io.nlopez.smartlocation.location.providers.LocationGooglePlayServicesProvider
import kotlinx.android.synthetic.main.masjid_home_screen.*
import rebus.permissionutils.PermissionEnum
import rebus.permissionutils.PermissionManager
import java.lang.Exception
import java.util.ArrayList

class MasjidHomeScreenActivity : BaseActivity(), MasjidHomeScreenView, OnMapReadyCallback {

    @BindView(R.id.toolbar)
    lateinit var mToolbar: Toolbar

    private var mMap: GoogleMap? = null
    private lateinit var mapView: MapView

    private lateinit var mClient: SettingsClient
    private lateinit var builder: LocationSettingsRequest.Builder
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private lateinit var mLocationRequest: LocationRequest
    private var marker: Marker? = null
    private var marker2: Marker? = null

    private lateinit var myLocation : LatLng

    private var bottomSheetFragment: MasjidListFragment = MasjidListFragment()

    private lateinit var mPresenter: MasjidHomeScreenPresenter

    companion object {
        fun getStartIntent(context: Context): Intent {
            return Intent(context, MasjidHomeScreenActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.masjid_home_screen)
        ButterKnife.bind(this)

        mapView = findViewById<MapView>(R.id.mapview)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        setup()
    }

    fun setup() {
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

        mPresenter = MasjidHomeScreenPresenter(this)

        dispatcher()
    }

    private fun dispatcher(){
        try{
            val intent = intent
            val b = intent.extras
            val act = b.getString("dispatcher")
            val id = b.getString("id")

            if(act == "masjid"){
                mPresenter.getDetailMasjid("1234")
            }else{
                Log.e("error intent", "no intent")
            }
        }catch (er: Exception){
            Log.e("error exception", "no intent")
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
                myLocation = LatLng(location.latitude, location.longitude)

                Log.e("Map", "latLng" + location.latitude + "," + location.longitude)

                marker?.position = myLocation
                mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 16f))

                mPresenter.getListMasjid(myLocation.latitude.toString(), myLocation.longitude.toString())

                SmartLocation.with(this@MasjidHomeScreenActivity).geocoding()
                        .reverse(location) { location1, list ->

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

    override fun showMasjid(list: MutableList<MasjidList>) {
        val builder = LatLngBounds.Builder();
        for (i in 0 until list.size) {
            marker2 = createMarker(list[i].lat.toDouble(), list[i].long.toDouble(), list[i].nama_tempat, list[i].alamat, list[i].id)
            builder.include(marker2?.position)
        }

        val bounds = builder.build();
        val cu = CameraUpdateFactory.newLatLngBounds(bounds, 0);
        mMap?.animateCamera(cu);
    }

    private fun createMarker(latitude: Double, longitude: Double, title: String, snippet: String, iconResID: String): Marker? {
        return mMap?.addMarker(MarkerOptions()
                .position(LatLng(latitude, longitude))
                .icon(BitmapDescriptorFactory.fromBitmap(CommonUtils.resizeMapIcons(this,"ic_marker_masjid",45,65)))
                .anchor(0.5f, 0.5f)
                .title(title)
                .snippet(snippet))

    }

    @OnClick(R.id.layout_tap, R.id.layout_tap2)
    fun onTap(view: View) {
        layout_tap.visibility = View.GONE
        tv_tap.visibility = View.GONE
        tv_menu.visibility = View.GONE
        tv_nearby.visibility = View.GONE
        tv_add.visibility = View.GONE
        tv_refresh.visibility = View.GONE
        tv_koordinat.visibility = View.GONE
    }

    @OnClick(R.id.img_menu)
    fun onMenu() {
        val intent = MasjidMenu.getStartIntent(this)
        startActivity(intent)
    }

    @OnClick(R.id.img_nearby)
    fun onNearby() {
        val intent = MasjidNearbyScreenActivity.getStartIntent(this)
        startActivity(intent)
    }

    @OnClick(R.id.img_add)
    fun onAdd() {
        val intent = MasjidAddActivity.getStartIntent(this)
        startActivity(intent)
    }

    @OnClick(R.id.img_refresh)
    fun onRefreshLocation(){
        requestLocation()
    }

    @OnClick(R.id.img_my_location)
    fun onMyLocation(){
        mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 16f))
    }

    @OnClick(R.id.btn_show)
    fun onShowList() {
        bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
    }

    fun hideBottomSheet() {
        if (bottomSheetFragment.isVisible) {
            bottomSheetFragment.dismiss()
        }
    }

    fun openSetting() {
        val intent = MasjidSettingActivity.getStartIntent(this)
        startActivity(intent)
    }

    override fun openDetail(obj: MasjidList) {
        val intent = MasjidDetailActivity.getStartIntent(this)
        intent.putExtra("obj", obj)
        startActivity(intent)
    }

    fun openTracking(obj: MasjidList) {
        val intent = MasjidDirectionsActivity.getStartIntent(this)
        intent.putExtra("obj", obj)
        startActivity(intent)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        Log.e("Map", "onMapReady")

        val latLng = LatLng(0.0, 0.0)
        //marker = mMap?.addMarker(MarkerOptions().position(latLng))

        marker = mMap?.addMarker(MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.fromBitmap(CommonUtils.resizeMapIcons(this,"ic_marker_origin_elipse_blue",45,45)))
                .anchor(0.5f, 0.5f)
                .title("Posisi anda"))
                //.snippet("Alamat anda"))

        mMap?.moveCamera(CameraUpdateFactory.newLatLng(latLng))
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionManager.handleResult(this, requestCode, permissions, grantResults)
    }
}
