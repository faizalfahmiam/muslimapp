package com.dmg.muslimapp.ui.masjid.home

interface MasjidHomeScreenPresenterView {
    fun getListMasjid(lat: String, long: String)
    fun getDetailMasjid(id: String)
    fun onDestroy()
}