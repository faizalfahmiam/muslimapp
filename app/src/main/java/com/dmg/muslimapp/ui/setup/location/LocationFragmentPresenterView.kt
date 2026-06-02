package com.dmg.muslimapp.ui.setup.location

import android.content.Context
import com.dmg.muslimapp.data.db.model.users.UserLocation

interface LocationFragmentPresenterView {
    fun setLocation(context: Context, userLocation: UserLocation)
    fun getDetailPlace(placeId: String)
    fun appConfig()
    fun onDestroy()
}