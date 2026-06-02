package com.dmg.muslimapp.ui.splash

class SplashModel {
    interface OnFinishedListener {
        fun onResultSuccess(nama: String)
        fun onResultFail(strError: String)
    }

    fun requestValue(onFinishedListener: OnFinishedListener){
        // Get data from server

    }
}