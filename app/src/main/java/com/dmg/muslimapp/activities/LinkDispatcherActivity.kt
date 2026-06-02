package com.dmg.muslimapp.activities

import android.os.Bundle
import android.util.Log
import com.dmg.muslimapp.BuildConfig
import com.dmg.muslimapp.ui.base.BaseActivity

class LinkDispatcherActivity: BaseActivity(){
    private val mMapper = UriToIntentMapper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            mMapper.dispatchIntent(intent)

        } catch (iae: IllegalArgumentException) {
            // Malformed URL
            if (BuildConfig.DEBUG) {
                Log.e("Deep links", "Invalid URI", iae)
            }
        } finally {
            // Always finish the Activity so that it doesn't stay in our history
            finish()
        }
    }
}