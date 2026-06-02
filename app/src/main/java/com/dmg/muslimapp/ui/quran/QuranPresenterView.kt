package com.dmg.muslimapp.ui.quran

interface QuranPresenterView {
    fun getAyahFromSura(index: Int)
    fun onDestroy()
}