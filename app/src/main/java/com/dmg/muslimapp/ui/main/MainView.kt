package com.dmg.muslimapp.ui.main

import com.dmg.muslimapp.data.db.model.quran.Ayah
import com.dmg.muslimapp.data.db.model.service_menu.ServiceMenu
import com.dmg.muslimapp.data.network.model.SliderHome

interface MainView {
    fun updateSliderResource(listData: List<SliderHome.SliderData>)
    fun showMenu(listMenu: List<ServiceMenu>)
    fun showFeedVerse(listAyah : List<Ayah>)
    fun onDetailVerse()
}