package com.dmg.muslimapp.ui.setup.language

import android.content.Context
import android.util.Log
import com.dmg.muslimapp.R
import com.dmg.muslimapp.data.prefs.AppPreference
import com.franmontiel.localechanger.LocaleChanger
import java.util.*


class LanguageFragmentPresenter(private var mView: LanguageFragmentView?): LanguageFragmentPresenterView {
    override fun onPreparedLanguage(context: Context){
        val languages = Arrays.asList(*context.resources.getStringArray(R.array.languages));

        if (languages != null) {
            for(item in languages){
                Log.e("item", item)
            }

            mView?.updateLanguage(languages)
        }else{
            Log.e("list","null")
        }
    }

    override fun onLocaleSelected(locale: String){
        AppPreference.setLanguage(locale)
        LocaleChanger.setLocale(Locale(locale))
    }

}
