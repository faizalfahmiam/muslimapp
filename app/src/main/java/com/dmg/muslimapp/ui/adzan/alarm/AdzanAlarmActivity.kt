package com.dmg.muslimapp.ui.adzan.alarm

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import android.support.v4.app.TaskStackBuilder
import android.support.v4.content.LocalBroadcastManager
import android.view.View
import android.view.WindowManager
import android.widget.RemoteViews

import java.util.Calendar
import java.util.TimeZone

import butterknife.ButterKnife
import butterknife.OnClick
import com.dmg.muslimapp.R
import com.dmg.muslimapp.data.db.AppDbHandler
import com.dmg.muslimapp.receiver.NotificationDismissedReceiver
import com.dmg.muslimapp.service.AdzanSoundService
import com.dmg.muslimapp.service.StopAdzanService
import com.dmg.muslimapp.ui.adzan.AdzanActivity
import com.dmg.muslimapp.ui.base.BaseActivity
import com.dmg.muslimapp.ui.main.MainActivity
import com.dmg.muslimapp.utils.AdzanSoundStore
import com.dmg.muslimapp.utils.CommonUtils
import kotlinx.android.synthetic.main.adzan_alarm.*

import com.dmg.muslimapp.utils.Constants.Companion.NOTIFICATION_ID

class AdzanAlarmActivity : BaseActivity() {
    /*@BindView(R.id.title)
    internal var mTitle: TextView? = null

    @BindView(R.id.message)
    internal var mMessage: TextView? = null

    @BindView(R.id.text_namashalat)
    internal var mNamaSholat: TextView? = null

    @BindView(R.id.text_waktushalat)
    internal var mWaktuSholat: TextView? = null*/

    private var mediaPlayer: MediaPlayer? = null
    private var notifType: Int = 0
    private var isFajr = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.adzan_alarm)
        ButterKnife.bind(this)
        setUp()
        instace = this
    }

    fun setUp() {
        LocalBroadcastManager.getInstance(this).registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                finish()
            }
        }, IntentFilter("onStopAdzan"))
        onAttachedToWindow()

        val inflater = getLayoutInflater()

        val intent = getIntent()
        //        Bundle b = intent.getExtras();

        var prayerName = intent.getStringExtra("prayerName")
        //        String title = intent.getStringExtra("title");
        //        String message = intent.getStringExtra("message");
        val isCustom = intent.getBooleanExtra("isCustom", false)
        notifType = intent.getIntExtra("notifType", 0)

        var message = ""
        //val userLocation = DbHandler.getInstance().config(DbHandler.userConfig).getLocation()
        val db = AppDbHandler(applicationContext)
        val userLoc = db.getUserLocation()

        val location = CommonUtils.getShortAddressCity(userLoc)
        val now = Calendar.getInstance(TimeZone.getDefault())
        now.timeInMillis = System.currentTimeMillis()
        val formatString = getString(R.string.notif_adzan_24_hour)
        val title = String.format(formatString, generateName(this, prayerName), now)

        if (prayerName == "Imsak") {
            prayerName = getString(R.string.imsak)
            message = String.format(getString(R.string.notification_body), getString(R.string.fajr), location)
        } else if (prayerName == "Fajr") {
            prayerName = getString(R.string.fajr)
            message = String.format(getString(R.string.notification_body), getString(R.string.fajr), location)
            isFajr = true
        } else if (prayerName == "Sunrise") {
            prayerName = getString(R.string.sunrise)
            message = String.format(getString(R.string.notification_body), getString(R.string.sunset), location)
        } else if (prayerName == "Dhuhr") {
            prayerName = getString(R.string.dhuhr)
            message = String.format(getString(R.string.notification_body), getString(R.string.dhuhr), location)
        } else if (prayerName == "Asr") {
            prayerName = getString(R.string.asr)
            message = String.format(getString(R.string.notification_body), getString(R.string.asr), location)
        } else if (prayerName == "Sunset") {
            prayerName = getString(R.string.sunset)
            message = String.format(getString(R.string.notification_body), getString(R.string.sunset), location)
        } else if (prayerName == "Maghrib") {
            prayerName = getString(R.string.maghrib)
            message = String.format(getString(R.string.notification_body), getString(R.string.maghrib), location)
        } else if (prayerName == "Isha") {
            prayerName = getString(R.string.isha)
            message = String.format(getString(R.string.notification_body), getString(R.string.isha), location)
        }

        sendNotification(this, prayerName, title, message, isCustom)
        //mTitle!!.setText(title)
        //mMessage!!.text = message
        //mNamaSholat!!.setText(prayerName)
        tv_title.text = title
        tv_message.text = message
        tv_namashalat.text = prayerName

        try {
            val split = title.split(" ".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
            val timeNow = split[2]
            //mWaktuSholat!!.setText(timeNow)
            tv_waktushalat.text = timeNow
        } catch (er: Exception) {
            //mWaktuSholat!!.text = ""
            tv_waktushalat.text = ""
        }

        if (notifType == AdzanSoundStore.Companion.NotifType.SOUND) {
            if (isFajr) {
                mediaPlayer = MediaPlayer.create(this, R.raw.adzan_fajr)
            } else {
                mediaPlayer = MediaPlayer.create(this, R.raw.adzan_mecca)
            }

            mediaPlayer!!.setVolume(100f, 100f)
            mediaPlayer!!.setOnCompletionListener { mediaPlayer -> finish() }

            if (!mediaPlayer!!.isPlaying) {
                mediaPlayer!!.start()
            }
        }
    }

    fun Close() {
        this.finish()
    }

    override fun onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer!!.stop()
            mediaPlayer!!.release()
        }
        super.onDestroy()
    }

    @OnClick(R.id.btnTurnOff)
    internal fun turnOff(v: View) {
        val notificationManager = getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()
        stopService(Intent(this, AdzanSoundService::class.java))
        finish()
    }

    override fun onAttachedToWindow() {
        val window = getWindow()
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                or WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                or WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD)
    }

    fun sendNotification(context: Context, prayerName: String, title: String, message: String, isCustom: Boolean) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val NOTIF_ID = Calendar.getInstance().timeInMillis.toInt()

        val contentIntent = TaskStackBuilder.create(context)
                .addNextIntent(MainActivity.getStartIntent(context))
                .addNextIntent(AdzanActivity.getStartIntent(context))
                .getPendingIntent(NOTIF_ID, PendingIntent.FLAG_CANCEL_CURRENT)

        //        Intent stopAdzanIntent = new Intent(context, StopAdzanService.class);
        //        PendingIntent pendingStopAdzanIntent = PendingIntent.getService(context, 1, stopAdzanIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification_transparent)
                .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher))
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(contentIntent)

        // if (!isCustom) {
        builder.setDeleteIntent(createOnDismissedIntent(context, 0))

        //        Intent backgroundSoundIntent = new Intent(context, AdzanSoundService.class);
        //        backgroundSoundIntent.putExtra(AdzanSoundService.EXTRA_IS_ADZAN, prayerName.equals(context.getString(R.string.fajr)));
        //        context.startService(backgroundSoundIntent);
        // } else {
        //     Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        ///     builder.setSound(uri);
        // }

        //New Coding 20181001 - Custom Layout Notification
        val remoteView = RemoteViews(context.packageName, R.layout.adzan_notification)
        setListeners(remoteView, context, title, message)
        builder.setContent(remoteView)
        //end new coding

        if (notifType == AdzanSoundStore.Companion.NotifType.VIBRATE) {
            builder.setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
        }

        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    private fun createOnDismissedIntent(context: Context, notificationId: Int): PendingIntent {
        val intent = Intent(context, NotificationDismissedReceiver::class.java)
        intent.putExtra("com.dmg.muslimapp", notificationId)

        return PendingIntent.getBroadcast(context.applicationContext,
                notificationId, intent, 0)
    }

    //New Coding 20181001 - Listener Notification
    fun setListeners(view: RemoteViews, context: Context, title: String, message: String) {
        //listener 1
        val intent = Intent(context.applicationContext, StopAdzanService::class.java)
        intent.putExtra("DO", "stop_adzan")
        val btn1 = PendingIntent.getService(context.applicationContext, NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        view.setTextViewText(R.id.notif_title, title)
        view.setTextViewText(R.id.notif_message, message)
        view.setOnClickPendingIntent(R.id.btn1, btn1)
    }

    private fun generateName(context: Context, name: String): String {
        return if (name == "Imsak") {
            context.getString(R.string.imsak)
        } else if (name == "Fajr") {
            context.getString(R.string.fajr)
        } else if (name == "Sunrise") {
            context.getString(R.string.sunrise)
        } else if (name == "Dhuhr") {
            context.getString(R.string.dhuhr)
        } else if (name == "Asr") {
            context.getString(R.string.asr)
        } else if (name == "Maghrib") {
            context.getString(R.string.maghrib)
        } else if (name == "Isha") {
            context.getString(R.string.isha)
        } else {
            name
        }
    }

    companion object {

        val CHANNEL_ID = "Adzan"

        var instace: AdzanAlarmActivity? = null
            private set

        fun getStartIntent(context: Context): Intent {
            val intent = Intent(context, AdzanAlarmActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            return intent
        }
    }
}
