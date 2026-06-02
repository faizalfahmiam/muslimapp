package com.dmg.muslimapp.ui.masjid.home

import com.dmg.muslimapp.data.network.model.masjid.MasjidList

interface MasjidHomeScreenView {
    fun showMasjid(list: MutableList<MasjidList>)
    fun openDetail(obj: MasjidList)
}