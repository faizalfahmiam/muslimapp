package com.dmg.muslimapp.ui.masjid.nearby.list

import com.dmg.muslimapp.data.network.model.masjid.MasjidNearbyList

interface MasjidNearbyListView {
    fun showPerson(list: MutableList<MasjidNearbyList>)
    fun showMasjidDetail(msg: String)
}