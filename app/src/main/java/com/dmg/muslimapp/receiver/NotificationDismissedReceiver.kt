package com.dmg.muslimapp.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.dmg.muslimapp.service.AdzanSoundService

class NotificationDismissedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notificationId = intent.extras!!.getInt("com.dmg.muslimapp")
        context.stopService(Intent(context, AdzanSoundService::class.java))
    }
}
