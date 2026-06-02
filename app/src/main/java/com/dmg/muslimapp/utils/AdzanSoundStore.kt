package com.dmg.muslimapp.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.dmg.muslimapp.data.model.Adzan
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.ArrayList

class AdzanSoundStore {

    companion object {
        var instance: AdzanSoundStore? = null
        lateinit var pref: SharedPreferences
        lateinit var editor: SharedPreferences.Editor
        const val SETTING_NAME = "ADZAN_SETTING"

        object NotifType {
            val NON_ACTIVE = -1
            val SOUND_OFF = 0
            val SOUND = 1
            val VIBRATE = 2
        }

        fun getInstance(context: Context): AdzanSoundStore? {
            if (instance == null) {
                pref = context.getSharedPreferences(SETTING_NAME, Context.MODE_PRIVATE)
                editor = pref.edit()
                instance = AdzanSoundStore()
            }
            return instance
        }

        fun getSetting(adzanName: String): Int {
            return pref.getInt(adzanName, 404)
        }

        fun addSetting(adzanName: String, notifType: Int) {
            // 0 = non active, 1 = sound, 2 = vibrate
            editor.putInt(adzanName, notifType).apply()
        }

        fun removeSetting(adzanName: String) {
            editor.remove(adzanName).apply()
        }

    }

    fun setCustomAdzan(adzanList: MutableList<Adzan>) {
        editor.putString(Constants.PREF_KEY_CUSTOM_ADZAN, Gson().toJson(adzanList)).apply()
    }

    fun getCustomAdzan(): MutableList<Adzan>? {
        Log.e("preference adzan","get")
        val json = pref.getString(Constants.PREF_KEY_CUSTOM_ADZAN, null)?: return null
        return Gson().fromJson<MutableList<Adzan>>(json, object : TypeToken<ArrayList<Adzan>>() {}.type)
    }
}