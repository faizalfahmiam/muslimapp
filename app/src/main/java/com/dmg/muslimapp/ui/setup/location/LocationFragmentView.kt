package com.dmg.muslimapp.ui.setup.location

import com.google.android.gms.location.places.Place

interface LocationFragmentView {
    fun onManualPlaceSeted(place: Place)
    fun configSuccess()
    fun configFailed()
    fun setLocationSuccess()
    fun setLocationFailed()
}