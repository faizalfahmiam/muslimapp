package com.dmg.muslimapp.ui.masjid.favorit

import com.dmg.muslimapp.data.network.model.masjid.MasjidList

interface MasjidListFavoritView {
    fun showMasjid(list: MutableList<MasjidList>)
}