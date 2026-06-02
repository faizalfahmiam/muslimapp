package com.dmg.muslimapp.ui.masjid.setting

import android.content.Context
import android.content.Intent
import android.os.Bundle
import butterknife.ButterKnife
import butterknife.OnClick
import com.dmg.muslimapp.R
import com.dmg.muslimapp.ui.base.BaseActivity

class MasjidSettingActivity: BaseActivity(){

    companion object {
        fun getStartIntent(context: Context): Intent {
            return Intent(context, MasjidSettingActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.masjid_setting)
        ButterKnife.bind(this)

        //setup()
    }

    @OnClick(R.id.img_close)
    fun onClose(){
        onBackPressed()
    }
}