package com.dmg.muslimapp.ui.login

import android.content.Context

interface LoginPresenterView {
    fun getLogin(context: Context, username: String, pass: String)
    fun getProfile(id: String)
    fun onDestroy()
}