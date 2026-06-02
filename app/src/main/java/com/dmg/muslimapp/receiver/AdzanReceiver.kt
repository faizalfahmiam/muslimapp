package com.dmg.muslimapp.receiver

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.support.v4.app.NotificationCompat
import android.support.v4.app.TaskStackBuilder
import android.util.Log
import com.dmg.muslimapp.R
import com.dmg.muslimapp.ui.adzan.AdzanActivity
import com.dmg.muslimapp.ui.adzan.alarm.AdzanAlarmActivity
import com.dmg.muslimapp.ui.adzan.alarm.AdzanAlarmActivity.Companion.CHANNEL_ID
import com.dmg.muslimapp.ui.main.MainActivity
import com.dmg.muslimapp.utils.AdzanSoundStore
import com.dmg.muslimapp.utils.AdzanStatusStore

import java.util.Calendar
import java.util.TimeZone
import com.dmg.muslimapp.utils.AdzanUtils
import com.dmg.muslimapp.utils.Constants.Companion.NOTIFICATION_ID

class AdzanReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.d(TAG, "AdzanReceiver ")

        try {
            //sendNotification(context, "This notification just for debugging", "AdzanReceiver onReceive");
            val adzanName = intent.getStringExtra("adzan_name")
            val adzanTime = intent.getStringExtra("adzan_time")
            Log.e("# Adzan receiver", "$adzanName $adzanTime")
            val cAdzanPlus5Minute = AdzanUtils.getCalendarFromPrayerTime(Calendar.getInstance(), adzanTime)
            cAdzanPlus5Minute.add(Calendar.MINUTE, 5)
            val cNow = Calendar.getInstance()

            if (cNow.after(cAdzanPlus5Minute)) {
                Log.e("# cAdzanPlus5Minute", "$adzanName $adzanTime")
                //sendNotification(context, "This notification just for debugging", "SL44");
                AdzanUtils.getTodayAdzanList(context)
                val nextAdzan = AdzanUtils.getNextAdzaan(context)
                AdzanUtils.setAlarm(context, nextAdzan.name!!, nextAdzan.time!!)
                return
            }

            val now = Calendar.getInstance(TimeZone.getDefault())
            now.timeInMillis = System.currentTimeMillis()
            val formatString = context.getString(R.string.notif_adzan_24_hour)

            Log.e("AdzanAlarmActivity", "AdzanAlarmActivity ")

            val intentLockscreen = AdzanAlarmActivity.getStartIntent(context)
            intentLockscreen.putExtra("prayerName", adzanName)
            //intentLockscreen.putExtra("title", String.format(formatString, generateName(context, adzan.name), now));
            //intentLockscreen.putExtra("message", message);
            intentLockscreen.putExtra("isCustom", false)
            intentLockscreen.putExtra("notifType", AdzanSoundStore.getSetting(adzanName))
            context.startActivity(intentLockscreen)

            setNextAdzan(context, adzanName)
        } catch (e: Exception) {
            //sendNotification(context, "This notification just for debugging", "at line " + e.getStackTrace()[0].getLineNumber() + " " + e.getMessage());
            Log.e("Alarm Error",e.toString())
        }

    }

    fun setNextAdzan(context: Context, currentAdzan: String) {
        AdzanStatusStore.getInstance(context)?.setStatus(currentAdzan, AdzanStatusStore.Val.PASSED)
        val nextAdzan = AdzanUtils.getNextAdzaan(context)
        AdzanUtils.setAlarm(context, nextAdzan.name!!, nextAdzan.time!!)
    }

    fun sendNotification(context: Context, title: String, message: String) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val NOTIF_ID = Calendar.getInstance().timeInMillis.toInt()

        val contentIntent = TaskStackBuilder.create(context)
                .addNextIntent(MainActivity.getStartIntent(context))
                .addNextIntent(AdzanActivity.getStartIntent(context))
                .getPendingIntent(NOTIF_ID, PendingIntent.FLAG_CANCEL_CURRENT)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification_transparent)
                .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher))
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(NotificationCompat.BigTextStyle()
                        .bigText(message))

                .setAutoCancel(true)
                .setContentIntent(contentIntent)

        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    companion object {
        val TAG = AdzanReceiver::class.java.simpleName
    }
}
