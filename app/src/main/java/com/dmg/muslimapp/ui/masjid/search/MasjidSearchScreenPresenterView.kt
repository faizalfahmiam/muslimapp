package com.dmg.muslimapp.ui.masjid.search

interface MasjidSearchScreenPresenterView {
    fun getListMasjid(lat: String, long: String)
    fun onDestroy()
}