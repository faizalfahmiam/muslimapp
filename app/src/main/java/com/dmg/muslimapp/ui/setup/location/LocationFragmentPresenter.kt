package com.dmg.muslimapp.ui.setup.location

import android.content.Context
import android.util.Log
import com.dmg.muslimapp.data.db.AppDbHandler
import com.dmg.muslimapp.data.db.model.users.UserLocation
import com.dmg.muslimapp.data.prefs.AppPreference
import com.google.android.gms.location.places.GeoDataClient

class LocationFragmentPresenter(private var mView: LocationFragmentView?): LocationFragmentPresenterView {
    private lateinit var context: Context

    private lateinit var mGeoDataClient: GeoDataClient

    fun setGeoDataClient(mGeoDataClient: GeoDataClient) {
        this.mGeoDataClient = mGeoDataClient
    }

    override fun getDetailPlace(placeId: String) {
        mGeoDataClient.getPlaceById(placeId).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val places = task.result
                val myPlace = places.get(0)
                mView?.onManualPlaceSeted(myPlace)
                Log.e("Place found: " , myPlace.name.toString())
                places.release()
            } else {
                Log.e("place", " not found")
            }
        }
    }

    override fun setLocation(context: Context, userLocation: UserLocation){
        val dbHandler = AppDbHandler(context)
        val insert = dbHandler.addOrUpdateUserLocation(userLocation)
        if(insert){
            val db2 = AppDbHandler(context)
            val userLoc = db2.getUserLocation()

            if(userLoc?.id.toString() != ""){
                AppPreference.setConfigured(true)
                mView?.setLocationSuccess()
            }else{
                mView?.setLocationFailed()
            }
        }else{
            mView?.setLocationFailed()
        }
    }

    override fun appConfig(){
        if(AppPreference.isAlreadyConfigured()){
            mView?.configSuccess()
        }else{
            mView?.configFailed()
        }
    }

    // Destroy View when Activity destroyed
    override fun onDestroy() {
        mView = null
    }
}