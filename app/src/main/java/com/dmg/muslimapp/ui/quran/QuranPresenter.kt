package com.dmg.muslimapp.ui.quran

import android.content.Context
import com.dmg.muslimapp.data.db.AppDbQuranHandler
import com.dmg.muslimapp.data.db.model.quran.Ayah

class QuranPresenter(private var mView: QuranView?, mContext: Context): QuranPresenterView {

    var dbHandler: AppDbQuranHandler = AppDbQuranHandler(mContext)

    override fun getAyahFromSura(index: Int){
        var listAyah: List<Ayah> = ArrayList<Ayah>()
        listAyah = dbHandler.getAllAyah(index)
        mView?.showAyah(listAyah)
    }

    // Destroy View when Activity destroyed
    override fun onDestroy() {
        mView = null
    }
}