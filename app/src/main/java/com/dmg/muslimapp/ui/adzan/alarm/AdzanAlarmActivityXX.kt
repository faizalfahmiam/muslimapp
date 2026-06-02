package com.dmg.muslimapp.ui.adzan.alarm

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import android.support.v4.app.TaskStackBuilder
import android.view.View
import android.view.WindowManager
import android.widget.RemoteViews

import java.util.Calendar

import butterknife.ButterKnife
import butterknife.OnClick
import com.dmg.muslimapp.R
import com.dmg.muslimapp.receiver.NotificationDismissedReceiver
import com.dmg.muslimapp.service.AdzanSoundService
import com.dmg.muslimapp.ui.adzan.AdzanActivity
import com.dmg.muslimapp.ui.base.BaseActivity
import com.dmg.muslimapp.ui.main.MainActivity
import com.dmg.muslimapp.utils.Constants.Companion.NOTIFICATION_ID
import kotlinx.android.synthetic.main.adzan_alarm.*

class AdzanAlarmActivityXX : BaseActivity() {
    /*@BindView(R.id.title)
    lateinit var mTitle: TextView

    @BindView(R.id.message)
    lateinit var mMessage: TextView

    @BindView(R.id.text_namashalat)
    lateinit var mNamaSholat: TextView

    @BindView(R.id.text_waktushalat)
    lateinit var mWaktuSholat: TextView*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.adzan_alarm)
        setUnBinder(ButterKnife.bind(this))
        setUp()
        instace = this
    }

    fun setUp() {

        onAttachedToWindow()

        val inflater = layoutInflater

        val intent = intent
        val b = intent.extras

        val prayerName = b!!.getString("prayerName")
        val title = b!!.getString("title")
        val message = b!!.getString("message")
        val isCustom = b!!.getBoolean("isCustom")
        sendNotification(this, prayerName, title, message, isCustom)

        //mTitle!!.text = title
        //mMessage!!.text = message
        //mNamaSholat!!.text = prayerName
        tv_title.text = title
        tv_message.text = message
        tv_namashalat.text = prayerName
        try {
            val split = title!!.split(" ".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
            val timeNow = split[2]
            //mWaktuSholat!!.text = timeNow
            tv_waktushalat.text = timeNow
        } catch (er: Exception) {
            //mWaktuSholat!!.text = ""
            tv_waktushalat.text = ""
        }

    }

    fun Close() {
        this.finish()
    }

    @OnClick(R.id.btnTurnOff)
    fun turnOff(v: View) {
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

    companion object {

        val CHANNEL_ID = "Adzan"

        var instace: AdzanAlarmActivityXX? = null
            private set

        fun getStartIntent(context: Context): Intent {
            val intent = Intent(context, AdzanAlarmActivityXX::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            return intent
        }

        fun sendNotification(context: Context, prayerName: String?, title: String?, message: String?, isCustom: Boolean) {
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

            notificationManager.notify(NOTIFICATION_ID, builder.build())
        }

        private fun createOnDismissedIntent(context: Context, notificationId: Int): PendingIntent {
            val intent = Intent(context, NotificationDismissedReceiver::class.java)
            intent.putExtra("com.dmg.muslimapp", notificationId)

            return PendingIntent.getBroadcast(context.applicationContext,
                    notificationId, intent, 0)
        }

        //New Coding 20181001 - Listener Notification
        fun setListeners(view: RemoteViews, context: Context, title: String?, message: String?) {
            //listener 1

            context.stopService(Intent(context, AdzanSoundService::class.java))
            //        Intent intent = new Intent(context.getApplicationContext(), StopAdzanService.class);
            //        intent.putExtra("DO", "stop_adzan");
            //        PendingIntent btn1 = PendingIntent.getService(context.getApplicationContext(), NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            //        view.setTextViewText(R.id.notif_title, title);
            //        view.setTextViewText(R.id.notif_message, message);
            //        view.setOnClickPendingIntent(R.id.btn1, btn1);
        }
    }
}
