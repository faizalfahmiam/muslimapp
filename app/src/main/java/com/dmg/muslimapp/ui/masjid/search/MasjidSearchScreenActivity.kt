package com.dmg.muslimapp.ui.masjid.search

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import android.widget.EditText
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.OnTextChanged
import com.dmg.muslimapp.R
import com.dmg.muslimapp.data.network.model.masjid.MasjidList
import com.dmg.muslimapp.ui.base.BaseActivity
import com.dmg.muslimapp.ui.masjid.detail.MasjidDetailActivity
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
import rebus.permissionutils.PermissionEnum
import rebus.permissionutils.PermissionManager
import java.util.ArrayList

class MasjidSearchScreenActivity : BaseActivity(), MasjidSearchScreenView, OnMapReadyCallback {

    @BindView(R.id.toolbar)
    lateinit var mToolbar: Toolbar

    @BindView(R.id.tv_search)
    lateinit var mTvSearch: EditText

    private var mMap: GoogleMap? = null
    private lateinit var mapView: MapView

    private lateinit var mClient: SettingsClient
    private lateinit var builder: LocationSettingsRequest.Builder
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private lateinit var mLocationRequest: LocationRequest
    private var marker: Marker? = null
    private var marker2: Marker? = null

    private lateinit var myLocation : LatLng

    private lateinit var mPresenter: MasjidSearchScreenPresenter

    private var mAdapter: MasjidSearchListAdapter? = null
    private var linearLayoutManager: LinearLayoutManager? = null

    @BindView(R.id.recyclerView)
    lateinit var mRecycleView: RecyclerView

    companion object {
        fun getStartIntent(context: Context): Intent {
            return Intent(context, MasjidSearchScreenActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.masjid_search_screen)
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

        mPresenter = MasjidSearchScreenPresenter(this)
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

                SmartLocation.with(this@MasjidSearchScreenActivity).geocoding()
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
        val cu = CameraUpdateFactory.newLatLngBounds(bounds, 0)
        mMap?.animateCamera(cu)

        showListMasjid(list)
    }

    fun showListMasjid(list: MutableList<MasjidList>){
        mAdapter = MasjidSearchListAdapter(applicationContext, list)

        linearLayoutManager = LinearLayoutManager(this)
        mRecycleView.setHasFixedSize(true);
        mRecycleView.layoutManager = linearLayoutManager
        mRecycleView.adapter = mAdapter

        mAdapter?.setOnClickListener(object : MasjidSearchListAdapter.OnClickListener {

            override fun onDetail(view: View, obj: MasjidList, pos: Int) {
                openDetail(obj)
            }
        })
    }

    private fun createMarker(latitude: Double, longitude: Double, title: String, snippet: String, iconResID: String): Marker? {
        return mMap?.addMarker(MarkerOptions()
                .position(LatLng(latitude, longitude))
                .icon(BitmapDescriptorFactory.fromBitmap(CommonUtils.resizeMapIcons(this,"ic_marker_masjid",45,65)))
                .anchor(0.5f, 0.5f)
                .title(title)
                .snippet(snippet))

    }

    fun openDetail(obj: MasjidList) {
        val intent = MasjidDetailActivity.getStartIntent(this)
        intent.putExtra("obj", obj)
        startActivity(intent)
    }

    /*@OnTextChanged(value = R.id.tv_search, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    fun nameChanged(text: CharSequence) {
        //do stuff
        Log.e("Serach",text.toString())
        mAdapter?.filter(text.toString())
    }*/

    @OnClick(R.id.img_clear_search)
    fun onClearSearch(){
        mTvSearch.text = null
        mAdapter?.filter("")
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
