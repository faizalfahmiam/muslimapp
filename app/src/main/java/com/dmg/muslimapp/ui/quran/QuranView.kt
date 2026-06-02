package com.dmg.muslimapp.ui.quran

import com.dmg.muslimapp.data.db.model.quran.Ayah

interface QuranView {
    fun showAyah(listAyah : List<Ayah>)
}