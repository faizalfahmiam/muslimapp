package com.dmg.muslimapp.ui.masjid.list

import com.dmg.muslimapp.data.network.model.masjid.MasjidList

interface MasjidListView {
    fun showMasjid(list: MutableList<MasjidList>)
    fun showMasjidDetail(msg: String)
}