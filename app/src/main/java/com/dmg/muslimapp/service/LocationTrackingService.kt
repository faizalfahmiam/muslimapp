package com.dmg.muslimapp.service

import android.app.Service
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.os.IBinder
import android.support.annotation.Nullable
import android.util.Log
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderApi
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationListener;
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import com.google.android.gms.common.ConnectionResult


class LocationTrackingService : Service(), GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private var mGoogleAPIClient: GoogleApiClient? = null
    private var mLocationRequest: LocationRequest? = null
    private var mLocationProvider: FusedLocationProviderApi? = null

    @Nullable
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        mGoogleAPIClient = GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build()

        mLocationProvider = LocationServices.FusedLocationApi

        mLocationRequest = LocationRequest()
        mLocationRequest!!.interval = 10000
        mLocationRequest!!.fastestInterval = 15000
        mLocationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        mGoogleAPIClient?.connect()
        return Service.START_STICKY
    }

    override fun onConnected(bundle: Bundle?) {
        RequestLocationUpdates()
    }

    private fun RequestLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleAPIClient, mLocationRequest, this) //getting error here..for casting..!

    }

    override fun onConnectionSuspended(i: Int) {

    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {

    }

    override fun onLocationChanged(location: Location) {
        Log.d("Background Location ", "::::***********Latitude: " + location.latitude + " Longitude: " + location.longitude)
    }
}