package com.dmg.muslimapp.service

import android.Manifest
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.IBinder
import android.support.annotation.Nullable
import android.support.v4.app.ActivityCompat
import android.util.Log
import io.nlopez.smartlocation.OnLocationUpdatedListener
import io.nlopez.smartlocation.SmartLocation
import io.nlopez.smartlocation.location.config.LocationParams
import io.nlopez.smartlocation.location.providers.LocationGooglePlayServicesProvider

class TrackerService: Service(), OnLocationUpdatedListener{

    private lateinit var infoLocation: String

    private lateinit var lastLocation: Location
    private lateinit var mLocationGooglePlayServicesProvider: LocationGooglePlayServicesProvider

    override fun onCreate() {
        super.onCreate()
        //getServiceComponent().inject(this)
    }

    @Nullable
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    fun getStartIntent(context: Context): Intent {
        return Intent(context, TrackerService::class.java)
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.i("TrackerService", "started")

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //if (getDataManager().getCurrentUserLoggedInMode() !== DataManager.LoggedInMode.LOGGED_IN_MODE_LOGGED_OUT.getType()) {
                getLocation()
            //}
        }

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("TrackerService","stopped")
        //getCompositeDisposable().dispose()

        stopLocation()

        val broadcastIntent = Intent("com.dmg.muslimapp.ActivityRecognition.RestartSensor")
        sendBroadcast(broadcastIntent)
    }

    fun start(context: Context) {
        val starter = Intent(context, TrackerService::class.java)
        context.startService(starter)
    }

    fun stop(context: Context) {
        context.stopService(Intent(context, TrackerService::class.java))
    }

    private fun getLocation() {
        val onNetwork = SmartLocation.with(this).location().state().isNetworkAvailable
        if (!onNetwork) {
            return
        }

        // Check if GPS is available
        val onGPS = SmartLocation.with(this).location().state().isGpsAvailable
        if (onGPS) {
            startLocation()
        }
    }

    private fun startLocation() {
        //mLocationManagerProvider = new LocationManagerProvider();

        mLocationGooglePlayServicesProvider = LocationGooglePlayServicesProvider()
        mLocationGooglePlayServicesProvider.setCheckLocationSettings(true)

        val smartLocation = SmartLocation.Builder(this).logging(true).build()
        smartLocation.location(mLocationGooglePlayServicesProvider)
                .config(LocationParams.LAZY).start(this)
    }

    private fun stopLocation() {
        SmartLocation.with(this).location().stop()
        SmartLocation.with(this).geofencing().stop()
    }

    private fun showLocation(location: Location) {
        Log.i("showLocation","Lat:" + location.latitude + ":Long" + location.longitude)
        lastLocation = location

        /*GeocoderUtils.reveseLocation(this, lastLocation, { original, results ->
            if (results.size() > 0) {
                val result = results.get(0)
                //mDataManager.saveDataLocation(lastLocation, result)

                val builder = StringBuilder()
                builder.append(result.getLocality()).append(", ")
                        .append(result.getSubAdminArea()).append(", ")
                        .append(result.getAdminArea()).append(", ")
                        .append(result.getPostalCode()).append(", ")
                        .append(result.getCountryName())

                infoLocation = builder.toString()
                sendLocation(lastLocation)
            }
        })*/
        sendLocation(lastLocation)
    }

    fun sendLocation(location: Location) {
        /*val request = TrackingRequest.SendPositionRequest()
        request.setIdUser(getDataManager().getCurrentUserId())
        request.setLat(location.latitude)
        request.setLong(location.longitude)
        request.setAddress(infoLocation)*/
        Log.i("sendLocation","Lat:" + location.latitude + ":Long" + location.longitude)
    }

    override fun onLocationUpdated(location: Location) {
        showLocation(location)
        Log.i("onLocationUpdated","Lat:" + location.latitude + ":Long" + location.longitude)
    }

    companion object {
        const val TAG = "TrackerService"
    }
}