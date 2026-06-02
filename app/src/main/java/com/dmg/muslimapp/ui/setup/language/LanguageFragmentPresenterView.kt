package com.dmg.muslimapp.ui.setup.language

import android.content.Context

interface LanguageFragmentPresenterView {
    fun onPreparedLanguage(context: Context)
    fun onLocaleSelected(locale: String)
}