package com.dmg.muslimapp.ui.login

interface LoginView {
    fun loginFailed(message: String)
    fun setProfileSuccess()
    fun setProfileFailed(message: String)
}