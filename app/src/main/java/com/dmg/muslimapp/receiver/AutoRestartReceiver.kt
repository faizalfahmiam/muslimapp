package com.dmg.muslimapp.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.dmg.muslimapp.service.TrackerService


class AutoRestartReceiver: BroadcastReceiver(){
    override fun onReceive(context: Context, arg1: Intent) {
        //val intent = Intent(context, LocationTrackingService::class.java)
        val intent = Intent(context, TrackerService::class.java)

        context.startService(intent)
        Log.i("Autostart", "started")
    }
}