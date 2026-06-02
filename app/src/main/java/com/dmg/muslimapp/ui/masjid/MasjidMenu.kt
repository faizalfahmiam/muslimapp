package com.dmg.muslimapp.ui.masjid

import android.content.Context
import android.content.Intent
import android.os.Bundle
import butterknife.ButterKnife
import butterknife.OnClick
import com.dmg.muslimapp.R
import com.dmg.muslimapp.ui.base.BaseActivity
import com.dmg.muslimapp.ui.masjid.add.MasjidAddActivity
import com.dmg.muslimapp.ui.masjid.favorit.MasjidListFavoritActivity
import com.dmg.muslimapp.ui.masjid.list.MasjidListFragment
import com.dmg.muslimapp.ui.masjid.search.MasjidSearchScreenActivity

class MasjidMenu: BaseActivity(){

    private var bottomSheetFragment: MasjidListFragment = MasjidListFragment()

    companion object {
        fun getStartIntent(context: Context): Intent {
            return Intent(context, MasjidMenu::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.masjid_menu)
        ButterKnife.bind(this)
        setup()
    }

    fun setup(){

    }

    @OnClick(R.id.btn_back)
    fun onBack(){
        onBackPressed()
    }

    @OnClick(R.id.tv_back)
    fun onBack2(){
        onBackPressed()
    }

    @OnClick(R.id.tv_favorit)
    fun onFavorit(){
        val intent = MasjidListFavoritActivity.getStartIntent(this)
        startActivity(intent)
    }

    @OnClick(R.id.tv_lokasi)
    fun onLocation(){
        bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
    }

    @OnClick(R.id.tv_cari)
    fun onCari(){
        val intent = MasjidSearchScreenActivity.getStartIntent(this)
        startActivity(intent)
    }

    @OnClick(R.id.tv_add)
    fun onAdd(){
        val intent = MasjidAddActivity.getStartIntent(this)
        startActivity(intent)
    }
}