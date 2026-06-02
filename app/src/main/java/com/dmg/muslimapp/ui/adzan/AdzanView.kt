package com.dmg.muslimapp.ui.adzan

import com.dmg.muslimapp.data.model.Adzan

interface AdzanView {
    fun showCustomAdzan(customAdzan: MutableList<Adzan>)
}