package com.dmg.muslimapp.ui.main

import android.content.Context

interface MainPresenterView {
    fun getSliderResources(context: Context, size: Int, locale: String)
    fun getServiceMenuList(context: Context)
    fun getFeedVerse(context: Context)
    fun onDestroy()
}