package com.dmg.muslimapp.ui.setup

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import butterknife.ButterKnife
import com.dmg.muslimapp.R
import com.dmg.muslimapp.ui.setup.language.LanguageFragment

class SetupActivity : AppCompatActivity(), SetupView {

    companion object {
        fun getStartIntent(context: Context): Intent {
            return Intent(context, SetupActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.setup_activity)
        ButterKnife.bind(this)

        setup()
    }

    fun setup(){
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        val fragment = LanguageFragment()
        transaction.add(R.id.content_layout, fragment)
        transaction.commit()
    }
}