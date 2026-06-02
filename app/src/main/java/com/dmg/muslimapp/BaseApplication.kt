package com.dmg.muslimapp

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.support.multidex.MultiDexApplication
import android.support.v7.app.AppCompatActivity
import com.androidnetworking.AndroidNetworking
import com.crashlytics.android.Crashlytics
import com.dmg.muslimapp.data.prefs.AppPreference
import com.dmg.muslimapp.receiver.AutoRestartReceiver
import com.dmg.muslimapp.utils.Constants
import com.franmontiel.localechanger.LocaleChanger
import com.google.android.gms.analytics.GoogleAnalytics
import com.google.android.gms.analytics.Tracker
import io.fabric.sdk.android.Fabric
import java.util.*

open class BaseApplication : MultiDexApplication() {

    private lateinit var mContext: Context
    private lateinit var mInstance: BaseApplication

    private lateinit var sAnalytics: GoogleAnalytics
    private lateinit var sTracker: Tracker

    val SUPPORTED_LOCALES = Arrays.asList(
            Locale("en"),
            Locale("in"),
            Locale("ar"),
            Locale("ms")
    )

    override fun onCreate() {
        super.onCreate()
        mInstance = this
        mContext = this

        if (!BuildConfig.DEBUG) {
            Fabric.with(this, Crashlytics())
        }

        sAnalytics = GoogleAnalytics.getInstance(this)

        LocaleChanger.initialize(applicationContext, SUPPORTED_LOCALES)
        AndroidNetworking.initialize(applicationContext)
        AppPreference.setup(this)

        setTrackingLocation()
        //createNotificationChannel()
    }

    /*private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Adzan"
            val description = "Notification for Adzan"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(AdzanJob.CHANNEL_ID, name, importance)
            channel.description = description
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = getContext()?.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }*/

    fun getInstance(): BaseApplication? {
        return mInstance
    }

    fun getContext(): Context? {
        return mContext
    }

    @Synchronized
    fun getDefaultTracker(): Tracker {
        // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
        if (sTracker == null) {
            sTracker = sAnalytics.newTracker(R.xml.global_tracker)
            sTracker.enableAdvertisingIdCollection(true)
        }

        return sTracker
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        LocaleChanger.onConfigurationChanged()
    }

    private fun setTrackingLocation() {
        val i = 2
        val intent = Intent(applicationContext, AutoRestartReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(applicationContext, Constants.CODE_TRACKING_LOCATION, intent, PendingIntent.FLAG_CANCEL_CURRENT)
        val alarmManager = getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, Calendar.getInstance().timeInMillis + i * 1000, 10000, pendingIntent)
    }
}
