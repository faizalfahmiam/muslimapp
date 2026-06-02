package com.dmg.muslimapp.ui.masjid.nearby

interface MasjidNearbyScreenPresenterView {
    fun getListMasjid(lat: String, long: String)
    fun onDestroy()
}