package com.dmg.muslimapp.ui.masjid.nearby

import com.dmg.muslimapp.data.network.model.masjid.MasjidList

interface MasjidNearbyScreenView {
    fun showMasjid(list: MutableList<MasjidList>)
}