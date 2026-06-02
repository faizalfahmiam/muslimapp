package com.dmg.muslimapp.ui.masjid.list

interface MasjidListPresenterView {
    fun getListMasjid()
    fun getDetailMasjid(id: String)
    fun onDestroy()
}