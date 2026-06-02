package com.dmg.muslimapp.ui.splash

interface SplashView {
    fun showForceUpdate(latestVersion: String)
    fun showRecommendUpdate(latestVersion: String)
    fun openMainActivity()
    fun openSetupActivity()
    fun setData(nama: String)
    fun setDataError(setError: String)
}