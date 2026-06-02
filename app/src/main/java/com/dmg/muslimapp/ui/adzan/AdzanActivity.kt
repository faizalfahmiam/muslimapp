package com.dmg.muslimapp.ui.adzan

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.dmg.muslimapp.R
import com.dmg.muslimapp.data.db.AppDbHandler
import com.dmg.muslimapp.data.model.Adzan
import com.dmg.muslimapp.data.prefs.AppPreference
import com.dmg.muslimapp.ui.base.BaseActivity
import com.dmg.muslimapp.utils.*
import java.util.*

class AdzanActivity : BaseActivity(), AdzanView, AdzanAdapter.OnItemClickListener {
    private lateinit var mPresenter: AdzanPresenter
    private var adzanAdapter: AdzanAdapter? = null
    private var timePrayers: MutableList<Adzan> = ArrayList()
    private var customAdzans: MutableList<Adzan> = ArrayList()
    private var myDays = 0
    private var myDaysHijri = 0
    private var ShowLayoutFormat = "list"

    @BindView(R.id.myScrollingContent)
    lateinit var nestedScrollView: NestedScrollView

    @BindView(R.id.recyclerView)
    lateinit var mRecyclerView: RecyclerView

    @BindView(R.id.fab_add)
    lateinit var mFabAdd: FloatingActionButton

    @BindView(R.id.tv_location)
    lateinit var tvLocation: TextView

    @BindView(R.id.tv_calendar)
    lateinit var mTvCalendar: TextView

    @BindView(R.id.tv_calendar_hijriyah)
    lateinit var mTvCalendarHijriyah: TextView

    @BindView(R.id.text_namashalat)
    lateinit var textNamashalat: TextView

    @BindView(R.id.text_waktushalat)
    lateinit var textWaktushalat: TextView

    companion object {
        fun getStartIntent(context: Context): Intent {
            return Intent(context, AdzanActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.adzan_activity)
        setUnBinder(ButterKnife.bind(this))

        setUp()
    }

    private fun setUp() {
        mPresenter = AdzanPresenter(this)
        mPresenter.getCustomPrayer(this)
        initPrayTimes()

        val userLocation = AppDbHandler(this).getUserLocation()

        tvLocation.setText(CommonUtils.getShortAddressCity(userLocation))
        tvLocation.setSelected(true)
        mTvCalendar.text = DateUtils.getCurrentDateMasehi(this, myDays)
        try {
            mTvCalendarHijriyah.text = DateUtils.getCurrentDateHijri(myDaysHijri, AppPreference.getLocale())
        } catch (er: Exception) {
        }


        nestedScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY > oldScrollY) {
                mFabAdd.hide()
            } else {
                mFabAdd.show()
            }
        })
    }

    fun initPrayTimes() {
        val adzan = AdzanUtils.getNextAdzaan(this)
        textWaktushalat.text = adzan.time
        //String remaining = "01:12:05"+" "+getString(R.string.time_remaining_adzan)+" "+prayer.getNamePray();
        //textNamashalat.setText(remaining);
        //countDownStart()
    }

    fun initPrayTimes(customAdzans: MutableList<Adzan>) {
        val c = Calendar.getInstance()
        c.add(Calendar.DATE, myDays)  // number of days to add

        //timePrayers = AdzanUtils.getTimeAdzan(this, c);

        Log.e("initPrayTimes", "AdzanUtils.getAdzanByCalendar(this, c)")
        timePrayers = AdzanUtils.getAdzanByCalendar(this, c)

        for (adzan in customAdzans) {
            timePrayers.add(adzan)
        }

        var mLayoutManager: RecyclerView.LayoutManager
        adzanAdapter = AdzanAdapter(timePrayers, baseContext, this, ShowLayoutFormat)
        mLayoutManager = LinearLayoutManager(applicationContext)
        if (ShowLayoutFormat == "grid") {
            mLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        }
        mRecyclerView.layoutManager = mLayoutManager
        mRecyclerView.itemAnimator = DefaultItemAnimator()
        mRecyclerView.adapter = adzanAdapter
    }

    override fun showCustomAdzan(customAdzans: MutableList<Adzan>) {
        Log.e("ADZAN","Show List Prayer")

        if (customAdzans != null)
            this.customAdzans = customAdzans
        initPrayTimes(this.customAdzans)
    }

    @OnClick(R.id.prevDay, R.id.nextDay)
    fun initPrayer(v: View) {
        when (v.id) {
            R.id.prevDay -> {
                myDays -= 1
                myDaysHijri -= 1
                mPresenter.getCustomPrayer(this)
                mTvCalendar.setText(DateUtils.getCurrentDateMasehi(this, myDays))
                try {
                    mTvCalendarHijriyah.text = DateUtils.getCurrentDateHijri(myDaysHijri, AppPreference.getLocale())
                } catch (er: Exception) {
                }

                AnimationUtils.slideInFromLeft(this, mRecyclerView)
            }
            R.id.nextDay -> {
                myDays += 1
                myDaysHijri += 1
                mPresenter.getCustomPrayer(this)
                mTvCalendar.setText(DateUtils.getCurrentDateMasehi(this, myDays))
                try {
                    mTvCalendarHijriyah.text = DateUtils.getCurrentDateHijri(myDaysHijri, AppPreference.getLocale())
                } catch (er: Exception) {
                }

                AnimationUtils.slideInFromRight(this, mRecyclerView)
            }
        }
        adzanAdapter?.setToday(myDays == 0)

    }

    /*private fun countDownStart() {
        runnable = object : Runnable {
            override fun run() {
                try {
                    //private String EVENT_DATE_TIME = "2018-12-31 10:30:00";
                    //private String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
                    val adzan = AdzanUtils.getNextAdzaan(applicationContext)

                    handler.postDelayed(this, 1000)

                    val df = SimpleDateFormat("yyyy-MM-dd")
                    val dateNow = df.format(DateUtils.today())

                    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    val event_date = dateFormat.parse(dateNow + " " + adzan.time + ":00")

                    AppLogger.e("date now : " + event_date.toString())

                    val current_date = Date()
                    if (!current_date.after(event_date)) {
                        val diff = event_date.time - current_date.time
                        //long Days = diff / (24 * 60 * 60 * 1000);
                        val Hours = diff / (60 * 60 * 1000) % 24
                        val Minutes = diff / (60 * 1000) % 60
                        val Seconds = diff / 1000 % 60
                        //

                        //String day = String.format("%02d", Days);
                        val hour = String.format("%02d", Hours)
                        val minute = String.format("%02d", Minutes)
                        val second = String.format("%02d", Seconds)
                        val remaining = hour + ":" + minute + ":" + second + " " + getString(R.string.time_remaining) + " " + AdzanUtils.getAdzanName(this@AdzanActivityNew, adzan.name)
                        textNamashalat.text = remaining
                        textWaktushalat.text = adzan.time
                    } else {
                        textNamashalat.setText(AdzanUtils.getAdzanName(this@AdzanActivityNew, adzan.name))
                        textWaktushalat.text = adzan.time
                        handler.removeCallbacks(runnable)
                    }

                    if (adzan.name.equals(getString(R.string.imsak).toLowerCase())) {
                        mLayoutHeader.setBackgroundResource(R.drawable.adzan_pic_header_subuh)
                    } else if (adzan.name.equals(getString(R.string.fajr).toLowerCase())) {
                        mLayoutHeader.setBackgroundResource(R.drawable.adzan_pic_header_subuh)
                    } else if (adzan.name.equals(getString(R.string.sunrise).toLowerCase())) {
                        mLayoutHeader.setBackgroundResource(R.drawable.adzan_pic_header_terbit)
                    } else if (adzan.name.equals(getString(R.string.dhuhr).toLowerCase())) {
                        mLayoutHeader.setBackgroundResource(R.drawable.adzan_pic_header_dhuhur)
                    } else if (adzan.name.equals(getString(R.string.asr).toLowerCase())) {
                        mLayoutHeader.setBackgroundResource(R.drawable.adzan_pic_header_ashar)
                    } else if (adzan.name.equals(getString(R.string.maghrib).toLowerCase())) {
                        mLayoutHeader.setBackgroundResource(R.drawable.adzan_pic_header_magrib)
                    } else if (adzan.name.equals(getString(R.string.isha).toLowerCase())) {
                        mLayoutHeader.setBackgroundResource(R.drawable.adzan_pic_header_isya)
                    } else {
                        mLayoutHeader.setBackgroundResource(R.drawable.adzan_pic_header_terbit)
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
        handler.postDelayed(runnable, 0)
    }*/

    @OnClick(R.id.fab_add)
    fun onAddPrayer(){
        val time_prayer = String.format("%s:%s", "08", "00")

//        AdzanSettings settings = AdzanSettings.getInstance();

        val drawables_new = intArrayOf(R.drawable.terbit_box)

        val adzan = Adzan()
        adzan.name = "Dhuha"
        adzan.custom = true
        adzan.time = time_prayer
        adzan.notif = AdzanSoundStore.Companion.NotifType.SOUND
//        DbHandler.config(DbHandler.adzanConfig).saveAdzan(adzan, false);

        adzanAdapter?.addAdzan(adzan)
    }

    override fun onDeletePrayer(position: Int, adzan: Adzan) {

    }

    override fun onSettingAlarm(adzan: Adzan, position: Int, sound: Boolean?) {
    }

    override fun onUpdateAlarm(name: String, position: Int, sound: Boolean?) {

    }
}