package com.dmg.muslimapp.ui.adzan

import android.content.Context
import com.dmg.muslimapp.data.model.Adzan
import com.dmg.muslimapp.utils.AdzanSoundStore

class AdzanPresenter (private var mView: AdzanView?) {

    fun getCustomPrayer(context: Context) {
        var list: MutableList<Adzan> = mutableListOf()

        if(AdzanSoundStore.getInstance(context)?.getCustomAdzan() != null) {
            list = AdzanSoundStore.getInstance(context)?.getCustomAdzan()!!
        }
        mView?.showCustomAdzan(list)
    }

}
