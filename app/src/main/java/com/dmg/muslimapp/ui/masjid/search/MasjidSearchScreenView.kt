package com.dmg.muslimapp.ui.masjid.search

import com.dmg.muslimapp.data.network.model.masjid.MasjidList

interface MasjidSearchScreenView {
    fun showMasjid(list: MutableList<MasjidList>)
}