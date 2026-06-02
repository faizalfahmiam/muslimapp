package com.dmg.muslimapp.ui.masjid.nearby.list

interface MasjidNearbyListPresenterView {
    fun getListMasjid()
    fun getDetailMasjid(id: String)
    fun onDestroy()
}