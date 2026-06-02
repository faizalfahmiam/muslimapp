package com.dmg.muslimapp.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.dmg.muslimapp.data.db.AppDbHandler
import com.dmg.muslimapp.data.model.Adzan
import com.dmg.muslimapp.receiver.AdzanReceiver
import java.util.*

class AdzanUtils {

    companion object {
        fun getTodayAdzanList(context: Context): MutableList<Adzan> {
            val defaultTz = TimeZone.getDefault()
            val defaultCalc = Calendar.getInstance(defaultTz)
            val defaultTzOffsetMs = defaultCalc.get(Calendar.ZONE_OFFSET) + defaultCalc.get(Calendar.DST_OFFSET)
            val timezone = (defaultTzOffsetMs / (1000 * 60 * 60)).toDouble()
            val prayers = PrayTime()
            val now = Date()
            val cal = Calendar.getInstance()
            cal.time = now

            val db = AppDbHandler(context)
            val userLoc = db.getUserLocation()
            val lat = userLoc?.latitude
            val lng = userLoc?.longitude

            //val lat = DbHandler.config(DbHandler.userConfig).getCurrentLat()
            //val lng = DbHandler.config(DbHandler.userConfig).getCurrentLong()

            val prayerTimes = prayers.getPrayerTime(cal, lat, lng, timezone)
            val customAdzans = AdzanSoundStore.getInstance(context)?.getCustomAdzan()

            if (customAdzans != null) {
                for (adzan in customAdzans) {
                    prayerTimes.add(adzan)
                }
            }

            for (adzan in prayerTimes) {
                if (AdzanSoundStore.getSetting(adzan.name!!) === 404) {
                    if (adzan.name.equals("Fajr") || adzan.name.equals("Dhuhr") ||
                            adzan.name.equals("Asr") || adzan.name.equals("Maghrib") ||
                            adzan.name.equals("Isha") || adzan.custom) {
                        AdzanSoundStore.addSetting(adzan.name!!, AdzanSoundStore.Companion.NotifType.SOUND)
                    } else {
                        AdzanSoundStore.addSetting(adzan.name!!, AdzanSoundStore.Companion.NotifType.NON_ACTIVE)
                    }
                }
            }

            return prayerTimes
        }

        fun getNextAdzaan(context: Context): Adzan {
            var nextAdzan: Adzan? = null
            val prayerTimes = getTodayAdzanList(context)
            var passedAll = true

            for (adzan in prayerTimes) {
                val timeHhMm = adzan.time!!.split(":")
                val now = Date()
                val cNow = Calendar.getInstance()
                cNow.time = now

                val cAdzan = Calendar.getInstance(TimeZone.getDefault())
                cAdzan.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeHhMm[0]))
                cAdzan.set(Calendar.MINUTE, Integer.parseInt(timeHhMm[1]))

                if (cAdzan.after(cNow)) {
                    if (AdzanSoundStore.getSetting(adzan.name!!) !== AdzanSoundStore.Companion.NotifType.NON_ACTIVE && AdzanStatusStore.getInstance(context)?.getStatus(adzan.name!!) === AdzanStatusStore.Val.NOT_PASSED) {
                        passedAll = false
                        nextAdzan = adzan
                        break
                    }
                } else {
                    Log.e("before ", adzan.name)
                    AdzanStatusStore.getInstance(context)?.setStatus(adzan.name!!, AdzanStatusStore.Val.PASSED)
                }
            }

            if (passedAll) {
                AdzanStatusStore.getInstance(context)?.resetStatus()
            }

            Log.e("passedAll", "$passedAll")

            // for tomorrow

            if (nextAdzan == null) {
                AdzanStatusStore.getInstance(context)?.resetStatus()
                nextAdzan = prayerTimes.get(0)
            }

            return nextAdzan
        }

        fun setAlarm(context: Context, adzanName: String, time: String) {
            Log.e("# setAlarm", "$adzanName $time")
            val times = time.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = System.currentTimeMillis()
            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(times[0]))
            calendar.set(Calendar.MINUTE, Integer.parseInt(times[1]))
            calendar.set(Calendar.SECOND, 2)

            val cNow = Calendar.getInstance()

            if (cNow.after(calendar)) {
                calendar.add(Calendar.DATE, 1)
                Log.d("buat besok " , calendar.time.toString())
            } else {
                Log.d("jadwal","buat hari ini")
            }

            val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context.applicationContext, AdzanReceiver::class.java)
            intent.putExtra("adzan_name", adzanName)
            intent.putExtra("adzan_time", time)
            val alarmIntent = PendingIntent.getBroadcast(context, 13119, intent, PendingIntent.FLAG_UPDATE_CURRENT)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmMgr.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, alarmIntent)
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmMgr.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, alarmIntent)
            } else {
                alarmMgr.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, alarmIntent)
            }

            //        AdzanNotificationJob.sheduleJob(adzanName, calendar);
        }

        fun getCalendarFromPrayerTime(cal: Calendar, prayerTime: String): Calendar {
            val time = prayerTime.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            cal.set(Calendar.HOUR_OF_DAY, Integer.valueOf(time[0]))
            cal.set(Calendar.MINUTE, Integer.valueOf(time[1]))
            cal.set(Calendar.SECOND, 1)
            cal.set(Calendar.MILLISECOND, 0)
            return cal
        }

        fun getAdzanByCalendar(context: Context, calendar: Calendar): MutableList<Adzan> {
            val defaultTzOffsetMs = calendar.get(Calendar.ZONE_OFFSET) + calendar.get(Calendar.DST_OFFSET)
            val timezone = (defaultTzOffsetMs / (1000 * 60 * 60)).toDouble()
            val prayers = PrayTime()

            val db = AppDbHandler(context)
            val userLoc = db.getUserLocation()
            val lat = userLoc?.latitude
            val lng = userLoc?.longitude

            //val lat = DbHandler.config(DbHandler.userConfig).getCurrentLat()
            //val lng = DbHandler.config(DbHandler.userConfig).getCurrentLong()

            val adzanList = prayers.getPrayerTime(calendar, lat, lng, timezone)
            val results = ArrayList<Adzan>()

            for (adzan in adzanList) {
                Log.e("adzan utils","getAdzanByCalendar : "+adzan.name +", Time: "+adzan.time)

                val result = Adzan()
                result.name = adzan.name
                result.time = adzan.time
                result.image = adzan.image
                result.custom = adzan.custom
                result.status = adzan.status

                if (AdzanSoundStore.getSetting(adzan.name!!) !== 404) {
                    result.notif = AdzanSoundStore.getSetting(adzan.name!!)
                }

                results.add(result)
            }

            return results
        }
    }
}