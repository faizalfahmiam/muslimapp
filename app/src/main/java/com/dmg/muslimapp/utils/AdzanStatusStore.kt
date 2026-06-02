package com.dmg.muslimapp.utils

import android.content.Context
import android.content.SharedPreferences

object AdzanStatusStore {

    private var instance: AdzanStatusStore? = null
    private var pref: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null
    val SETTING_NAME = "ADZAN_STATUS"

    object Val {
        val NOT_PASSED = 0
        val PASSED = 1
    }

    fun getInstance(context: Context): AdzanStatusStore? {
        if (instance == null) {
            pref = context.getSharedPreferences(SETTING_NAME, Context.MODE_PRIVATE)
            editor = pref!!.edit()
            instance = AdzanStatusStore
        }
        return instance
    }

    fun getStatus(adzanName: String): Int {
        return pref!!.getInt(adzanName, Val.NOT_PASSED)
    }

    fun setStatus(adzanName: String, status: Int) {
        editor!!.putInt(adzanName, status).apply()
    }

    fun resetStatus() {
        editor!!.clear().commit()
    }
}
