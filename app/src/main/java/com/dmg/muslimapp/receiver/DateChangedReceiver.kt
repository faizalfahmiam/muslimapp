package com.dmg.muslimapp.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.dmg.muslimapp.utils.AdzanStatusStore
import com.dmg.muslimapp.utils.AdzanUtils

class DateChangedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.e("Adzan Date","DATE_CHANGED")

        AdzanUtils.getTodayAdzanList(context)
        val nextAdzan = AdzanUtils.getNextAdzaan(context)
        AdzanUtils.setAlarm(context, nextAdzan.name!!, nextAdzan.time!!)
        AdzanStatusStore.getInstance(context)?.resetStatus()

        //        Toast.makeText(context, "DateChangedReceiver!", Toast.LENGTH_SHORT).show();
        //        Toast.makeText(context, "next adzan " + nextAdzan.name + " " + nextAdzan.time, Toast.LENGTH_LONG).show();
    }
}
