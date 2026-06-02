package com.dmg.muslimapp.ui.quran

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import butterknife.BindView
import butterknife.ButterKnife
import com.dmg.muslimapp.R
import com.dmg.muslimapp.data.db.model.quran.Ayah
import com.dmg.muslimapp.ui.base.BaseActivity
import com.dmg.muslimapp.utils.BottomBarUtils
import com.dmg.muslimapp.utils.ToastMessage
import kotlinx.android.synthetic.main.bottom_sheet.*

class QuranActivity: BaseActivity(), QuranView, QuranAyahAdapter.OnItemClickListener {
    private lateinit var mPresenter: QuranPresenter

    @BindView(R.id.toolbar)
    lateinit var mToolbar: Toolbar

    private var mAdapter: QuranAyahAdapter? = null
    private var recyclerView: RecyclerView? = null
    private var linearLayoutManager: LinearLayoutManager? = null

    companion object {
        fun getStartIntent(context: Context): Intent {
            return Intent(context, QuranActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.quran_activity)
        ButterKnife.bind(this)
        setup()
        mainOfContent()
    }

    fun setup(){
        setToolbar(mToolbar)
        showBackButton(true)

        BottomBarUtils(this, bottom_sheet, bottom_navigation_v, sv_service_menu)
        mPresenter = QuranPresenter(this, this)
    }

    fun mainOfContent(){
        mPresenter.getAyahFromSura(5)
    }

    override fun showAyah(listAyah : List<Ayah>){
        ToastMessage("List Ayah",this)
        for(aya  in listAyah){

        }


        recyclerView = findViewById(R.id.recyclerView) as RecyclerView
        mAdapter = QuranAyahAdapter(applicationContext, listAyah, this)
        linearLayoutManager = LinearLayoutManager(applicationContext)
        (recyclerView as RecyclerView).layoutManager = linearLayoutManager
        (recyclerView as RecyclerView).adapter = mAdapter
    }

    override fun onDetail() {

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            /*R.id.setting -> {
                val intent = SettingActivity.getStartIntent(this)
                startActivity(intent)
                return true
            }*/
        }
        return super.onOptionsItemSelected(item)
    }
}