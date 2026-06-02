package com.dmg.muslimapp.data.prefs

import android.content.Context
import android.content.SharedPreferences
import com.dmg.muslimapp.utils.Constants
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.ArrayList
import com.dmg.muslimapp.data.network.model.SliderHome
import com.dmg.muslimapp.utils.LoggedStatus


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class AppPreference {

    companion object {
        private lateinit var mPrefs: SharedPreferences

        fun setup(context: Context) {
            this.mPrefs = context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE)

        }

        fun isAlreadyConfigured(): Boolean {
            return mPrefs.getBoolean(Constants.PREF_KEY_ALREADY_CONFIGURED, false)
        }

        fun setConfigured(status: Boolean) {
            mPrefs.edit().putBoolean(Constants.PREF_KEY_ALREADY_CONFIGURED, status).apply()
        }

        fun setLanguage(locale: String) {
            mPrefs.edit().putString(Constants.SET_LOCALE, locale).apply()
        }

        fun getLatesVersion(): String {
            return mPrefs.getString(Constants.SET_LATES_APP_VERSION, "")
        }

        fun setLatesVersion(version: String) {
            mPrefs.edit().putString(Constants.SET_LATES_APP_VERSION, version).apply()
        }

        fun getLocale(): String {
            return mPrefs.getString(Constants.SET_LOCALE, "")
        }

        fun getSlider(): List<SliderHome.SliderData>? {
            val response: String = mPrefs.getString(Constants.PREF_KEY_SLIDER, null) ?: return null
            return Gson().fromJson(response, object : TypeToken<ArrayList<SliderHome.SliderData>>() {}.type)

        }

        fun setSlider(sliderResponse: String) {
            mPrefs.edit().putString(Constants.PREF_KEY_SLIDER, sliderResponse).apply()
        }

        fun getCurrentUserLoggedInMode(): Int {
            return mPrefs.getInt(Constants.PREF_KEY_USER_LOGGED_IN_MODE, LoggedStatus.LoggedInMode.LOGGED_IN_MODE_LOGGED_OUT.type)
        }

        fun setCurrentUserLoggedInMode(mode: LoggedStatus.LoggedInMode) {
            mPrefs.edit().putInt(Constants.PREF_KEY_USER_LOGGED_IN_MODE, mode.type).apply()
        }

        fun getMenuVersion(): Int{
            return mPrefs.getInt(Constants.PREF_KEY_MENU_VERSION, 0)
        }

        fun setMenuVersion(version: Int) {
            mPrefs.edit().putInt(Constants.PREF_KEY_MENU_VERSION, version).apply()
        }

    }
}