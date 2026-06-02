package com.dmg.muslimapp.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

import com.dmg.muslimapp.utils.AdzanUtils

class AdzanBootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.e("Adzan Boot","BOOT_COMPLETED!")
        AdzanUtils.getTodayAdzanList(context)
        val nextAdzan = AdzanUtils.getNextAdzaan(context)
        AdzanUtils.setAlarm(context, nextAdzan.name!!, nextAdzan.time!!)

        //        Toast.makeText(context, "BOOT_COMPLETED", Toast.LENGTH_LONG).show();
        //        Toast.makeText(context, "next adzan " + nextAdzan.name + " " + nextAdzan.time, Toast.LENGTH_LONG).show();


        //        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        //        Intent i = new Intent(context, ReschedulaAdzanReceiver.class);
        //        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 555, i, PendingIntent.FLAG_UPDATE_CURRENT);
        //
        //        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        //        calendar.setTimeInMillis(System.currentTimeMillis());
        //        calendar.add(Calendar.MINUTE, 15);
        //
        //        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        //            alarmMgr.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
        //        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        //            alarmMgr.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
        //        } else {
        //            alarmMgr.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
        //        }

    }
}
