package com.dmg.muslimapp.ui.main

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dmg.muslimapp.R
import com.dmg.muslimapp.data.db.model.quran.Ayah
import com.dmg.muslimapp.data.db.model.service_menu.ServiceMenu
import com.dmg.muslimapp.data.model.Adzan
import com.dmg.muslimapp.data.network.model.SliderHome
import com.dmg.muslimapp.data.prefs.AppPreference
import com.dmg.muslimapp.di.module.GlideApp
import com.dmg.muslimapp.receiver.AutoRestartReceiver
import com.dmg.muslimapp.ui.adzan.AdzanActivity
import com.dmg.muslimapp.ui.base.BaseActivity
import com.dmg.muslimapp.ui.custom.viewpager.AutoScrollViewPager
import com.dmg.muslimapp.ui.custom.viewpager.BaseViewPagerAdapter
import com.dmg.muslimapp.ui.main.verse.VerseAdapter
import com.dmg.muslimapp.ui.masjid.home.MasjidHomeScreenActivity
import com.dmg.muslimapp.ui.quran.QuranActivity
import com.dmg.muslimapp.utils.AdzanUtils
import com.dmg.muslimapp.utils.CommonUtils
import com.dmg.muslimapp.utils.Constants.Companion.CODE_TRACKING_LOCATION
import com.google.gson.Gson
import kotlinx.android.synthetic.main.main_activity.*
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : BaseActivity(), MainView, VerseAdapter.OnItemClickListener {
    private lateinit var mPresenter: MainPresenter

    private lateinit var sliderData: List<SliderHome.SliderData>
    private lateinit var mBaseViewPagerAdapter: BaseViewPagerAdapter<SliderHome.SliderData>

    private lateinit var mMainMenuServiceAdapter: MainMenuServiceAdapter
    private lateinit var mVerseAdapter: VerseAdapter

    @BindView(R.id.viewPager)
    lateinit var mScrollViewPager: AutoScrollViewPager

    @BindView(R.id.recycleView)
    lateinit var mRecyclerView: RecyclerView

    @BindView(R.id.recycleView2)
    lateinit var mRecyclerView2: RecyclerView

    companion object {
        fun getStartIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        ButterKnife.bind(this)
        setup()
    }

    fun setup(){
        //val intent = Intent(this, LocationTrackingService::class.java)
        //this.startService(intent)
        setTrackingLocation()

        mPresenter = MainPresenter(this)
        //mPrefs = AppPreference(this)
        //BottomBarUtils(this, bottom_sheet, bottom_navigation_v, sv_service_menu)

        getSliderBanner()
        mScrollViewPager.setAdapter(mBaseViewPagerAdapter)

        layout_time_remaining.bringToFront()
        dispatcher()

        adzanConfig()
    }

    private fun dispatcher(){
        try{
            val intent = intent
            val b = intent.extras
            val act = b.getString("dispatcher")
            val id = b.getString("id")

            if(act == "masjid"){
                val intent = MasjidHomeScreenActivity.getStartIntent(this)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.putExtra("dispatcher", "masjid")
                intent.putExtra("id", id)
                startActivity(intent)
            }else{
                Log.e("error intent", "no intent")
            }
        }catch (er: Exception){
            Log.e("error exception", "no intent")
        }
    }

    override fun onResume() {
        super.onResume()
        Log.i("onResume", "onResume")

        if (isNetworkConnected()) {
            if (AppPreference.getSlider() != null && mBaseViewPagerAdapter.count == 0) {
                val sliderResponses = ArrayList<SliderHome.SliderData>()
                for (response : SliderHome.SliderData in AppPreference.getSlider()!!) {
                    sliderResponses.add(response)
                }
                mBaseViewPagerAdapter.add(sliderResponses)
            } else {
                initSliderDefault()
            }

            mPresenter.getSliderResources(this, 2, AppPreference.getLocale())
        } else if (AppPreference.getSlider() != null && mBaseViewPagerAdapter.count == 0) {
            val sliderResponses = ArrayList<SliderHome.SliderData>()
            for (response: SliderHome.SliderData in AppPreference.getSlider()!!) {
                sliderResponses.add(response)
            }
            mBaseViewPagerAdapter.add(sliderResponses)
        } else {
            initSliderDefault()
        }


        /*if (!mDataManager.isLocationManual()) {
            askPermission(false)
        }*/

        mPresenter.getServiceMenuList(this)
        mPresenter.getFeedVerse(this)
    }

    private fun getSliderBanner(){
        mBaseViewPagerAdapter = object : BaseViewPagerAdapter<SliderHome.SliderData>(this) {
            override fun loadImage(view: ImageView, position: Int, sliderResponse: SliderHome.SliderData) {
                GlideApp.with(this@MainActivity)
                        .load(sliderResponse.getGambar())
                        .override(view.width, view.height)
                        //.transform(MultiTransformation(CenterCrop(), RoundedCorners(10)))
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .error(R.drawable.nopict)
                        .into(view)

                view.setOnClickListener { _ ->
                    val intent: Intent

                    if (!(sliderResponse.getUrl() as java.lang.String).equalsIgnoreCase("-")) {
                        if ((sliderResponse.getUrl() as java.lang.String).startsWith("http://") || (sliderResponse.getUrl() as java.lang.String).startsWith("https://")) {
                            intent = Intent(Intent.ACTION_VIEW, Uri.parse(sliderResponse.getUrl()))
                            startActivity(intent)
                        } else if ((sliderResponse.getUrl() as java.lang.String).startsWith("muslimapp://")) {
                            val page = (sliderResponse.getUrl() as java.lang.String).split("//")[1]
                            when (page) {
                                "live_mecca" -> {
                                    //intent = LiveMeccaActivity.getStartIntent(this@MainActivity)
                                    //startActivity(intent)
                                    Log.e("Banner",page)
                                }
                                //"tracking" -> mPresenter.openTrackingActivity()
                                "aqiqah" -> {
                                    //intent = AqiqahActivity.getStartIntent(this@MainActivity)
                                    //startActivity(intent)
                                    Log.e("Banner",page)
                                }
                                "qurban" -> {
                                    //intent = QurbanActivity.getStartIntent(this@MainActivity)
                                    //startActivity(intent)
                                    Log.e("Banner",page)
                                }
                                "bursa_sajadah" -> {
                                    //intent = BursaSajadahActivity.getStartIntent(this@MainActivity)
                                    //startActivity(intent)
                                    Log.e("Banner",page)
                                }
                            }
                        }
                    }

                }
            }

            override fun setSubTitle(textView: TextView, position: Int, sliderResponse: SliderHome.SliderData) {

            }
        }
    }

    private fun initSliderDefault() {
        if (mBaseViewPagerAdapter.count == 0 || mBaseViewPagerAdapter == null) {
            val sliderResponses = ArrayList<SliderHome.SliderData>()
            val gambar: String = CommonUtils.getURLForResource(this, R.drawable.slider_default)
            sliderResponses.add(SliderHome.SliderData("-", gambar))

            for(aya in sliderResponses){
                Log.e("gambar", aya.getUrl())
                Log.e("gambar", aya.getGambar())
            }

            mBaseViewPagerAdapter.init(sliderResponses)

        }else{
            Log.e("gambar", mBaseViewPagerAdapter.count.toString())
        }
    }

    override fun updateSliderResource(listData: List<SliderHome.SliderData>) {
        val sliderResponses = Gson().toJson(listData)
        AppPreference.setSlider(sliderResponses)
        sliderData = listData
        mBaseViewPagerAdapter.init(sliderData)
    }

    private fun setTrackingLocation() {
        val i = 2
        val intent = Intent(applicationContext, AutoRestartReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(applicationContext, CODE_TRACKING_LOCATION, intent, PendingIntent.FLAG_CANCEL_CURRENT)
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, Calendar.getInstance().timeInMillis + i * 1000, 10000, pendingIntent)
    }

    override fun showMenu(listMenu: List<ServiceMenu>){
        for(menu in listMenu){
            Log.e("Show Menu ", menu.menu_index.toString() +", "+menu.menu_id+", "+menu.menu_status)
        }

        mMainMenuServiceAdapter = MainMenuServiceAdapter(this, listMenu, 0)
        mRecyclerView.adapter = mMainMenuServiceAdapter
        val manager = GridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false)
        mRecyclerView.layoutManager = manager
    }

    override fun showFeedVerse(listAyah : List<Ayah>){
        for(aya  in listAyah){
            Log.e("Feed Ayah", aya.AyahText)
            Log.e("Feed Arti", aya.Translation)
        }

        mVerseAdapter = VerseAdapter(applicationContext, listAyah, this)
        mRecyclerView2.adapter = mVerseAdapter
        val manager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView2.layoutManager = manager
    }

    fun adzanConfig(){
        val nextAdzan: Adzan = AdzanUtils.getNextAdzaan(this)
        AdzanUtils.setAlarm(this, nextAdzan.name!!, nextAdzan.time!!)
    }

    override fun onDetailVerse() {

    }

    fun openLainnyaActivity(){
        //
    }

    fun openQuranActivity(){
        val intent = QuranActivity.getStartIntent(this)
        startActivity(intent)
    }

    fun openMasjidOnBoardingActivity(){
        //val intent = MasjidOnBoardingActivity.getStartIntent(this)
        val intent = MasjidHomeScreenActivity.getStartIntent(this)
        startActivity(intent)
    }

    fun openAdzanActivity(){
        val intent = AdzanActivity.getStartIntent(this)
        startActivity(intent)
    }

    /*@OnClick(R.id.btn_quran)
    fun openQuran(){
        //val intent = QuranActivity.getStartIntent(this)
        val intent = LoginActivity.getStartIntent(this)
        //val intent = ScannerActivity.getStartIntent(this)
        startActivity(intent)
    }

    @OnClick(R.id.btn_locale)
    fun setLocale(){
        val locale = tv_locale.text.toString()

        AppPreference.setLanguage(locale)
        LocaleChanger.setLocale(Locale("in"))
        val intent = MainActivity.getStartIntent(this@MainActivity)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }*/
}