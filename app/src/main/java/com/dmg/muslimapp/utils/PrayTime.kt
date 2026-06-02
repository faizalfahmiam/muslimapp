package com.dmg.muslimapp.utils

import android.content.Context
import android.util.Log

import com.dmg.muslimapp.data.model.Adzan
import com.dmg.muslimapp.utils.AdzanUtils.Companion.getCalendarFromPrayerTime

import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Calendar
import java.util.Date
import java.util.HashMap
import java.util.LinkedHashMap
import java.util.TimeZone

class PrayTime {

    // ---------------------- Global Variables --------------------
    var calcMethod: Int = 0 // caculation method
    var asrJuristic: Int = 0 // Juristic method for Asr
    var dhuhrMinutes: Int = 0 // minutes after mid-day for Dhuhr
    var adjustHighLats: Int = 0 // adjusting method for higher latitudes
    var timeFormat: Int = 0 // time format
    var lat: Double = 0.toDouble() // latitude
    var lng: Double = 0.toDouble() // longitude
    var timeZone: Double = 0.toDouble() // time-zone
    var jDate: Double = 0.toDouble() // Julian date
    // Time Names
    val timeNames: ArrayList<String>
    private val InvalidTime: String // The string used for invalid times
    // --------------------- Technical Settings --------------------
    private var numIterations: Int = 0 // number of iterations needed to compute times
    // ------------------- Calc Method Parameters --------------------
    private val methodParams: HashMap<Int, DoubleArray>

    /*
     * this.methodParams[methodNum] = new Array(fa, ms, mv, is, iv);
     *
     * fa : fajr angle ms : maghrib selector (0 = angle; 1 = minutes after
     * sunset) mv : maghrib parameter value (in angle or minutes) is : isha
     * selector (0 = angle; 1 = minutes after maghrib) iv : isha parameter value
     * (in angle or minutes)
     */
    private val prayerTimesCurrent: DoubleArray? = null
    private val offsets: IntArray

    // ---------------------- Time-Zone Functions -----------------------
    // compute local time-zone for a specific date
    private val timeZone1: Double
        get() {
            val timez = TimeZone.getDefault()
            return timez.rawOffset / 1000.0 / 3600
        }

    // compute base time-zone of the system
    private val baseTimeZone: Double
        get() {
            val timez = TimeZone.getDefault()
            return timez.rawOffset / 1000.0 / 3600

        }

    init {
        // Initialize vars
        this.calcMethod = 0
        this.asrJuristic = 0
        this.dhuhrMinutes = 0
        this.adjustHighLats = 1
        this.timeFormat = 0

        // Time Names
        timeNames = ArrayList()
        timeNames.add("Fajr")
        timeNames.add("Sunrise")
        timeNames.add("Dhuhr")
        timeNames.add("Asr")
        timeNames.add("Sunset")
        timeNames.add("Maghrib")
        timeNames.add("Isha")

        InvalidTime = "-----" // The string used for invalid times

        // --------------------- Technical Settings --------------------

        this.numIterations = 1 // number of iterations needed to compute
        // times

        // ------------------- Calc Method Parameters --------------------

        // Tuning offsets {fajr, sunrise, dhuhr, asr, sunset, maghrib, isha}
        offsets = IntArray(7)
        offsets[0] = 0
        offsets[1] = 0
        offsets[2] = 0
        offsets[3] = 0
        offsets[4] = 0
        offsets[5] = 0
        offsets[6] = 0

        /*
         *
         * fa : fajr angle ms : maghrib selector (0 = angle; 1 = minutes after
         * sunset) mv : maghrib parameter value (in angle or minutes) is : isha
         * selector (0 = angle; 1 = minutes after maghrib) iv : isha parameter
         * value (in angle or minutes)
         */
        methodParams = HashMap()

        // JAFARI
        val Jvalues = doubleArrayOf(16.0, 0.0, 4.0, 0.0, 14.0)
        methodParams[JAFARI] = Jvalues

        // KARACHI
        val Kvalues = doubleArrayOf(18.0, 1.0, 0.0, 0.0, 18.0)
        methodParams[KARACHI] = Kvalues

        // ISNA
        val Ivalues = doubleArrayOf(15.0, 1.0, 0.0, 0.0, 15.0)
        methodParams[ISNA] = Ivalues

        // MWL
        val MWvalues = doubleArrayOf(18.0, 1.0, 0.0, 0.0, 17.0)
        methodParams[MWL] = MWvalues

        // MAKKAH
        val MKvalues = doubleArrayOf(18.5, 1.0, 0.0, 1.0, 90.0)
        methodParams[MAKKAH] = MKvalues

        // EGYPT
        val Evalues = doubleArrayOf(19.5, 1.0, 0.0, 0.0, 17.5)
        methodParams[EGYPT] = Evalues

        // TEHRAN
        val Tvalues = doubleArrayOf(17.7, 0.0, 4.5, 0.0, 14.0)
        methodParams[TEHRAN] = Tvalues

        // CUSTOM
        val Cvalues = doubleArrayOf(20.0, 1.0, 0.0, 0.0, 18.0)
        methodParams[CUSTOM] = Cvalues

    }

    // ---------------------- Trigonometric Functions -----------------------
    // range reduce angle in degrees.
    private fun fixangle(a: Double): Double {
        var a = a

        a = a - 360 * Math.floor(a / 360.0)

        a = if (a < 0) a + 360 else a

        return a
    }

    // range reduce hours to 0..23
    private fun fixhour(a: Double): Double {
        var a = a
        a = a - 24.0 * Math.floor(a / 24.0)
        a = if (a < 0) a + 24 else a
        return a
    }

    // radian to degree
    private fun radiansToDegrees(alpha: Double): Double {
        return alpha * 180.0 / Math.PI
    }

    // deree to radian
    private fun DegreesToRadians(alpha: Double): Double {
        return alpha * Math.PI / 180.0
    }

    // degree sin
    private fun dsin(d: Double): Double {
        return Math.sin(DegreesToRadians(d))
    }

    // degree cos
    private fun dcos(d: Double): Double {
        return Math.cos(DegreesToRadians(d))
    }

    // degree tan
    private fun dtan(d: Double): Double {
        return Math.tan(DegreesToRadians(d))
    }

    // degree arcsin
    private fun darcsin(x: Double): Double {
        val `val` = Math.asin(x)
        return radiansToDegrees(`val`)
    }

    // degree arccos
    private fun darccos(x: Double): Double {
        val `val` = Math.acos(x)
        return radiansToDegrees(`val`)
    }

    // degree arctan
    private fun darctan(x: Double): Double {
        val `val` = Math.atan(x)
        return radiansToDegrees(`val`)
    }

    // degree arctan2
    private fun darctan2(y: Double, x: Double): Double {
        val `val` = Math.atan2(y, x)
        return radiansToDegrees(`val`)
    }

    // degree arccot
    private fun darccot(x: Double): Double {
        val `val` = Math.atan2(1.0, x)
        return radiansToDegrees(`val`)
    }

    // detect daylight saving in a given date
    private fun detectDaylightSaving(): Double {
        val timez = TimeZone.getDefault()
        return timez.dstSavings.toDouble()
    }

    // ---------------------- Julian Date Functions -----------------------
    // calculate julian date from a calendar date
    private fun julianDate(year: Int, month: Int, day: Int): Double {
        var year = year
        var month = month

        if (month <= 2) {
            year -= 1
            month += 12
        }
        val A = Math.floor(year / 100.0)

        val B = 2 - A + Math.floor(A / 4.0)

        return (Math.floor(365.25 * (year + 4716))
                + Math.floor(30.6001 * (month + 1)) + day.toDouble() + B) - 1524.5
    }

    // convert a calendar date to julian date (second method)
    private fun calcJD(year: Int, month: Int, day: Int): Double {
        val J1970 = 2440588.0
        val date = Date(year, month - 1, day)

        val ms = date.time.toDouble() // # of milliseconds since midnight Jan 1,
        // 1970
        val days = Math.floor(ms / (1000.0 * 60.0 * 60.0 * 24.0))
        return J1970 + days - 0.5

    }

    // ---------------------- Calculation Functions -----------------------
    // References:
    // http://www.ummah.net/astronomy/saltime
    // http://aa.usno.navy.mil/faq/docs/SunApprox.html
    // compute declination angle of sun and equation of time
    private fun sunPosition(jd: Double): DoubleArray {

        val D = jd - 2451545
        val g = fixangle(357.529 + 0.98560028 * D)
        val q = fixangle(280.459 + 0.98564736 * D)
        val L = fixangle(q + 1.915 * dsin(g) + 0.020 * dsin(2 * g))

        // double R = 1.00014 - 0.01671 * [self dcos:g] - 0.00014 * [self dcos:
        // (2*g)];
        val e = 23.439 - 0.00000036 * D
        val d = darcsin(dsin(e) * dsin(L))
        var RA = darctan2(dcos(e) * dsin(L), dcos(L)) / 15.0
        RA = fixhour(RA)
        val EqT = q / 15.0 - RA
        val sPosition = DoubleArray(2)
        sPosition[0] = d
        sPosition[1] = EqT

        return sPosition
    }

    // compute equation of time
    private fun equationOfTime(jd: Double): Double {
        return sunPosition(jd)[1]
    }

    // compute declination angle of sun
    private fun sunDeclination(jd: Double): Double {
        return sunPosition(jd)[0]
    }

    // compute mid-day (Dhuhr, Zawal) time
    private fun computeMidDay(t: Double): Double {
        val T = equationOfTime(this.jDate + t)
        return fixhour(12 - T)
    }

    // compute time for a given angle G
    private fun computeTime(G: Double, t: Double): Double {

        val D = sunDeclination(this.jDate + t)
        val Z = computeMidDay(t)
        val Beg = -dsin(G) - dsin(D) * dsin(this.lat)
        val Mid = dcos(D) * dcos(this.lat)
        val V = darccos(Beg / Mid) / 15.0

        return Z + if (G > 90) -V else V
    }

    // compute the time of Asr
    // SHAFII: step=1, HANAFI: step=2
    private fun computeAsr(step: Double, t: Double): Double {
        val D = sunDeclination(this.jDate + t)
        val G = -darccot(step + dtan(Math.abs(this.lat - D)))
        return computeTime(G, t)
    }

    // ---------------------- Misc Functions -----------------------
    // compute the difference between two times
    private fun timeDiff(time1: Double, time2: Double): Double {
        return fixhour(time2 - time1)
    }

    // -------------------- Interface Functions --------------------
    // return prayer times for a given date
    private fun getDatePrayerTimes(year: Int, month: Int, day: Int,
                                   latitude: Double, longitude: Double, tZone: Double): ArrayList<String> {
        this.lat = latitude
        this.lng = longitude
        this.timeZone = tZone
        this.jDate = julianDate(year, month, day)
        val lonDiff = longitude / (15.0 * 24.0)
        this.jDate = this.jDate - lonDiff
        return computeDayTimes()
    }

    // return prayer times for a given date
    fun getPrayerTimes(date: Calendar, latitude: Double,
                       longitude: Double, tZone: Double): ArrayList<String> {

        val year = date.get(Calendar.YEAR)
        val month = date.get(Calendar.MONTH)
        val day = date.get(Calendar.DATE)

        return getDatePrayerTimes(year, month + 1, day, latitude, longitude, tZone)
    }

    // set custom values for calculation parameters
    private fun setCustomParams(params: DoubleArray) {

        for (i in 0..4) {
            if (params[i] == -1.0) {
                params[i] = methodParams[this.calcMethod]!![i]
                methodParams[CUSTOM] = params
            } else {
                //methodParams[CUSTOM][i] = params[i]
                methodParams[CUSTOM]!![i] = params[i]
            }
        }
        this.calcMethod = CUSTOM
    }

    // set the angle for calculating Fajr
    fun setFajrAngle(angle: Double) {
        val params = doubleArrayOf(angle, -1.0, -1.0, -1.0, -1.0)
        setCustomParams(params)
    }

    // set the angle for calculating Maghrib
    fun setMaghribAngle(angle: Double) {
        val params = doubleArrayOf(-1.0, 0.0, angle, -1.0, -1.0)
        setCustomParams(params)

    }

    // set the angle for calculating Isha
    fun setIshaAngle(angle: Double) {
        val params = doubleArrayOf(-1.0, -1.0, -1.0, 0.0, angle)
        setCustomParams(params)

    }

    // set the minutes after Sunset for calculating Maghrib
    fun setMaghribMinutes(minutes: Double) {
        val params = doubleArrayOf(-1.0, 1.0, minutes, -1.0, -1.0)
        setCustomParams(params)

    }

    // set the minutes after Maghrib for calculating Isha
    fun setIshaMinutes(minutes: Double) {
        val params = doubleArrayOf(-1.0, -1.0, -1.0, 1.0, minutes)
        setCustomParams(params)

    }

    // convert double hours to 24h format
    fun floatToTime24(time: Double): String {
        var time = time

        val result: String

        if (java.lang.Double.isNaN(time)) {
            return InvalidTime
        }

        time = fixhour(time + 0.5 / 60.0) // add 0.5 minutes to round
        val hours = Math.floor(time).toInt()
        val minutes = Math.floor((time - hours) * 60.0)

        if (hours >= 0 && hours <= 9 && minutes >= 0 && minutes <= 9) {
            result = hours.toString() + ":0" + Math.round(minutes)
        } else if (hours >= 0 && hours <= 9) {
            result = hours.toString() + ":" + Math.round(minutes)
        } else if (minutes >= 0 && minutes <= 9) {
            result = hours.toString() + ":0" + Math.round(minutes)
        } else {
            result = hours.toString() + ":" + Math.round(minutes)
        }
        return result
    }

    // convert double hours to 12h format
    fun floatToTime12(time: Double, noSuffix: Boolean): String {
        var time = time

        if (java.lang.Double.isNaN(time)) {
            return InvalidTime
        }

        time = fixhour(time + 0.5 / 60) // add 0.5 minutes to round
        var hours = Math.floor(time).toInt()
        val minutes = Math.floor((time - hours) * 60)
        val suffix: String
        var result: String
        if (hours >= 12) {
            suffix = "pm"
        } else {
            suffix = "am"
        }
        hours = (hours + 12 - 1) % 12 + 1
        /*hours = (hours + 12) - 1;
        int hrs = (int) hours % 12;
        hrs += 1;*/

        if (hours >= 0 && hours <= 9 && minutes >= 0 && minutes <= 9) {
            result = hours.toString() + ":0" + Math.round(minutes)
        } else if (hours >= 0 && hours <= 9) {
            result = hours.toString() + ":" + Math.round(minutes)
        } else if (minutes >= 0 && minutes <= 9) {
            result = hours.toString() + ":0" + Math.round(minutes)
        } else {
            result = hours.toString() + ":" + Math.round(minutes)
        }

        if (!noSuffix) {
            result += " $suffix"
        }

        return result

    }

    // convert double hours to 12h format with no suffix
    fun floatToTime12NS(time: Double): String {
        return floatToTime12(time, true)
    }

    // ---------------------- Compute Prayer Times -----------------------
    // compute prayer times at given julian date
    private fun computeTimes(times: DoubleArray): DoubleArray {

        val t = dayPortion(times)

        val Fajr = this.computeTime(
                180 - methodParams[this.calcMethod]!![0], t[0])

        val Sunrise = this.computeTime(180 - 0.833, t[1])

        val Dhuhr = this.computeMidDay(t[2])
        val Asr = this.computeAsr((1 + this.asrJuristic).toDouble(), t[3])
        val Sunset = this.computeTime(0.833, t[4])

        val Maghrib = this.computeTime(
                methodParams[this.calcMethod]!![2], t[5])
        val Isha = this.computeTime(
                methodParams[this.calcMethod]!![4], t[6])

        return doubleArrayOf(Fajr, Sunrise, Dhuhr, Asr, Sunset, Maghrib, Isha)

    }

    // compute prayer times at given julian date
    private fun computeDayTimes(): ArrayList<String> {
        var times = doubleArrayOf(5.0, 6.0, 12.0, 13.0, 18.0, 18.0, 18.0) // default times

        for (i in 1..this.numIterations) {
            times = computeTimes(times)
        }

        times = adjustTimes(times)
        times = tuneTimes(times)

        return adjustTimesFormat(times)
    }

    // adjust times in a prayer time array
    private fun adjustTimes(times: DoubleArray): DoubleArray {
        var times = times
        for (i in times.indices) {
            times[i] += this.timeZone - this.lng / 15
        }

        times[2] += (this.dhuhrMinutes / 60).toDouble() // Dhuhr
        if (methodParams[this.calcMethod]!![1] == 1.0)
        // Maghrib
        {
            times[5] = times[4] + methodParams[this.calcMethod]!![2] / 60
        }
        if (methodParams[this.calcMethod]!![3] == 1.0)
        // Isha
        {
            times[6] = times[5] + methodParams[this.calcMethod]!![4] / 60
        }

        if (this.adjustHighLats != NONE) {
            times = adjustHighLatTimes(times)
        }

        return times
    }

    // convert times array to given time format
    private fun adjustTimesFormat(times: DoubleArray): ArrayList<String> {

        val result = ArrayList<String>()

        if (this.timeFormat == FLOATING) {
            for (time in times) {
                result.add(time.toString())
            }
            return result
        }

        for (i in 0..6) {
            if (this.timeFormat == TIME_12) {
                result.add(floatToTime12(times[i], false))
            } else if (this.timeFormat == TIME_12_NS) {
                result.add(floatToTime12(times[i], true))
            } else {
                result.add(floatToTime24(times[i]))
            }
        }
        return result
    }

    // adjust Fajr, Isha and Maghrib for locations in higher latitudes
    private fun adjustHighLatTimes(times: DoubleArray): DoubleArray {
        val nightTime = timeDiff(times[4], times[1]) // sunset to sunrise

        // Adjust Fajr
        val FajrDiff = nightPortion(methodParams[this.calcMethod]!![0]) * nightTime

        if (java.lang.Double.isNaN(times[0]) || timeDiff(times[0], times[1]) > FajrDiff) {
            times[0] = times[1] - FajrDiff
        }

        // Adjust Isha
        //val IshaAngle: Double = if (methodParams[this.calcMethod]!![3] == 0.0) methodParams[this.calcMethod]!![4] else 18
        var IshaAngle = 0.0
        if(methodParams[this.calcMethod]!![3] == 0.0){
            IshaAngle = methodParams[this.calcMethod]!![4]
        }else{
            IshaAngle = 18.0
        }

        val IshaDiff: Double = this.nightPortion(IshaAngle) * nightTime
        if (java.lang.Double.isNaN(times[6]) || this.timeDiff(times[4], times[6]) > IshaDiff) {
            times[6] = times[4] + IshaDiff
        }

        // Adjust Maghrib
        //val MaghribAngle = if (methodParams[this.calcMethod]!![1] == 0.0) methodParams[this.calcMethod]!![2] else 4
        var MaghribAngle = 0.0
        if(methodParams[this.calcMethod]!![1] == 0.0){
            MaghribAngle = methodParams[this.calcMethod]!![2]
        }else{
            MaghribAngle = 4.0
        }

        val MaghribDiff: Double = nightPortion(MaghribAngle) * nightTime
        if (java.lang.Double.isNaN(times[5]) || this.timeDiff(times[4], times[5]) > MaghribDiff) {
            times[5] = times[4] + MaghribDiff
        }

        return times
    }

    // the night portion used for adjusting times in higher latitudes
    private fun nightPortion(angle: Double): Double {
        var calc = 0.0

        if (adjustHighLats == ANGLE_BASED)
            calc = angle / 60.0
        else if (adjustHighLats == MID_NIGHT)
            calc = 0.5
        else if (adjustHighLats == ONE_SEVENTH)
            calc = 0.14286

        return calc
    }

    // convert hours to day portions
    private fun dayPortion(times: DoubleArray): DoubleArray {
        for (i in 0..6) {
            times[i] /= 24.0
        }
        return times
    }

    // Tune timings for adjustments
    // Set time offsets
    fun tune(offsetTimes: IntArray) {

        for (i in offsetTimes.indices) { // offsetTimes length
            // should be 7 in order
            // of Fajr, Sunrise,
            // Dhuhr, Asr, Sunset,
            // Maghrib, Isha
            this.offsets[i] = offsetTimes[i]
        }
    }

    private fun tuneTimes(times: DoubleArray): DoubleArray {
        for (i in times.indices) {
            times[i] = times[i] + this.offsets[i] / 60.0
        }

        return times
    }

    fun getPrayerTime(date: Calendar, latitude: Double?, longitude: Double?, tZone: Double): MutableList<Adzan> {

        Log.e("set adzan","prayer")

        val results = ArrayList<Adzan>()
        val year = date.get(Calendar.YEAR)
        val month = date.get(Calendar.MONTH)
        val day = date.get(Calendar.DATE)

        timeFormat = TIME_24
        calcMethod = CUSTOM
        asrJuristic = SHAFII
        adjustHighLats = ANGLE_BASED
        val offsets = intArrayOf(0, 0, 0, 0, 0, 0, 0) // {Fajr,Sunrise,Dhuhr,Asr,Sunset,Maghrib,Isha}
        tune(offsets)

        val prayerTimes = getDatePrayerTimes(year, month + 1, day, latitude!!, longitude!!, tZone)
        val prayerNames = timeNames

        for (i in prayerTimes.indices) {
            //System.out.println(prayerNames.get(i) + " - " + prayerTimes.get(i));

            val t = prayerTimes[i].split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val hour = Integer.parseInt(t[0])
            val minute = Integer.parseInt(t[1])

            var totalMinute = 0

            if (prayerNames[i].toLowerCase() == "dhuhr")
                totalMinute = hour * 60 + minute + 3
            else if (prayerNames[i].toLowerCase() == "sunrise")
                totalMinute = hour * 60 + minute - 3
            else if (prayerNames[i].toLowerCase() == "maghrib") {
                totalMinute = hour * 60 + minute + 4
            } else
                totalMinute = hour * 60 + minute + 2

            val minuteLeft = totalMinute % 60
            val hourLeft = totalMinute / 60

            val customTime: String

            if (minuteLeft.toString().length < 2)
                customTime = hourLeft.toString() + ":0" + minuteLeft
            else
                customTime = hourLeft.toString() + ":" + minuteLeft

            Log.e("set adzan","prayerTimes : "+prayerNames[i] +", Time: "+customTime)

            val adzan = Adzan()
            adzan.name = prayerNames[i]
            adzan.time = customTime
            results.add(adzan)
        }

        return results
    }

    companion object {
        // ------------------------------------------------------------
        // Calculation Methods
        val JAFARI = 0 // Ithna Ashari
        val KARACHI = 1 // University of Islamic Sciences, KARACHI
        val ISNA = 2 // Islamic Society of North America (ISNA)
        val MWL = 3 // Muslim World League (MWL)
        val MAKKAH = 4 // Umm al-Qura, MAKKAH
        val EGYPT = 5 // Egyptian General Authority of Survey
        val CUSTOM = 7 // CUSTOM Setting
        val TEHRAN = 6 // Institute of Geophysics, University of TEHRAN
        // Juristic Methods
        val SHAFII = 0 // SHAFII (standard)
        val HANAFI = 1 // HANAFI
        // Adjusting Methods for Higher Latitudes
        val NONE = 0 // No adjustment
        val MID_NIGHT = 1 // middle of night
        val ONE_SEVENTH = 2 // 1/7th of night
        val ANGLE_BASED = 3 // angle/60th of night
        // Time Formats
        val TIME_24 = 0 // 24-hour format
        val TIME_12 = 1 // 12-hour format
        val TIME_12_NS = 2 // 12-hour format with no suffix
        val FLOATING = 3 // floating point number

        /**
         * @param args
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val latitude = -33.8736779
            val longitude = 151.196515

            //Get NY time zone instance
            val defaultTz = TimeZone.getDefault()

            //Get NY calendar object with current date/time
            val defaultCalc = Calendar.getInstance(defaultTz)

            //Get offset from UTC, accounting for DST
            val defaultTzOffsetMs = defaultCalc.get(Calendar.ZONE_OFFSET) + defaultCalc.get(Calendar.DST_OFFSET)
            val timezone = (defaultTzOffsetMs / (1000 * 60 * 60)).toDouble()
            // Test Prayer times here
            val prayers = PrayTime()

            prayers.timeFormat = TIME_12
            prayers.calcMethod = KARACHI
            prayers.asrJuristic = SHAFII
            prayers.adjustHighLats = ANGLE_BASED

            val offsets = intArrayOf(0, 0, 0, 0, 0, 0, 0) // {Fajr,Sunrise,Dhuhr,Asr,Sunset,Maghrib,Isha}
            prayers.tune(offsets)

            val now = Date()
            val cal = Calendar.getInstance()
            cal.time = now

            val prayerTimes = prayers.getPrayerTimes(cal,
                    latitude, longitude, timezone)
            val prayerNames = prayers.timeNames

            for (i in prayerTimes.indices) {
                println("PrayTime Adzan for main " + prayerNames[i] + " - " + prayerTimes[i])
            }

        }

        @JvmOverloads
        fun getPrayerTimes(context: Context, index: Int, lat: Double, lng: Double, timeFormat: Int = -1): LinkedHashMap<String, String> {
            val settings = AdzanSettings.getInstance(context)

            //Get time zone instance
            val defaultTz = TimeZone.getDefault()

            //Get calendar object with current date/time
            val defaultCalc = Calendar.getInstance(defaultTz)

            //Get offset from UTC, accounting for DST
            val defaultTzOffsetMs = defaultCalc.get(Calendar.ZONE_OFFSET) + defaultCalc.get(Calendar.DST_OFFSET)
            val timezone = (defaultTzOffsetMs / (1000 * 60 * 60)).toDouble()

            // Test Prayer times here
            val prayers = PrayTime()

            if (timeFormat == -1) {
                prayers.timeFormat = settings.getTimeFormatFor(index)
            } else {
                prayers.timeFormat = timeFormat
            }

            prayers.calcMethod = settings.getCalcMethodSetFor(index)
            prayers.asrJuristic = settings.getAsrMethodSetFor(index)
            prayers.adjustHighLats = settings.getHighLatitudeAdjustmentFor(index)

            val offsets = intArrayOf(0, 0, 0, 0, 0, 0, 0) // {Fajr,Sunrise,Dhuhr,Asr,Sunset,Maghrib,Isha}
            prayers.tune(offsets)

            val now = Date()
            val cal = Calendar.getInstance()
            cal.time = now

            val prayerTimes = prayers.getPrayerTimes(cal, lat, lng, timezone)
            val prayerNames = prayers.timeNames
            val result = LinkedHashMap<String, String>()

            var preSuhoorTime = Calendar.getInstance(TimeZone.getDefault())
            preSuhoorTime.timeInMillis = System.currentTimeMillis()
            val time = (10 * 60 * 1000).toLong()
            preSuhoorTime = getCalendarFromPrayerTime(preSuhoorTime, prayerTimes[0])
            preSuhoorTime.timeInMillis = preSuhoorTime.timeInMillis - time + 2 * 60 * 1000

            val df = SimpleDateFormat("H:mm")
            val imsakTime = df.format(preSuhoorTime.time)

            println("Imsak - $imsakTime")
            result["Imsak"] = imsakTime

            for (i in prayerTimes.indices) {
                println("PrayTime Adzan for getPrayerTimes : " + prayerNames[i] + " - " + prayerTimes[i])

                val t = prayerTimes[i].split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val hour = Integer.parseInt(t[0])
                val minute = Integer.parseInt(t[1])

                var totalMinute = 0

                if (prayerNames[i].toLowerCase() == "dhuhr")
                    totalMinute = hour * 60 + minute + 3
                else if (prayerNames[i].toLowerCase() == "sunrise")
                    totalMinute = hour * 60 + minute - 3
                else if (prayerNames[i].toLowerCase() == "maghrib") {
                    totalMinute = hour * 60 + minute + 4
                } else
                    totalMinute = hour * 60 + minute + 2

                val minuteLeft = totalMinute % 60
                val hourLeft = totalMinute / 60

                val customTime: String

                if (minuteLeft.toString().length < 2)
                    customTime = hourLeft.toString() + ":0" + minuteLeft
                else
                    customTime = hourLeft.toString() + ":" + minuteLeft


                println("Custom :" + prayerNames[i] + " - " + customTime)

                result[prayerNames[i]] = customTime
            }

            return result
        }

        fun getPrayerTimes(context: Context, calendar: Calendar, index: Int, lat: Double, lng: Double, timeFormat: Int): LinkedHashMap<String, String> {
            val settings = AdzanSettings.getInstance(context)

            //Get time zone instance
            val defaultTz = TimeZone.getDefault()

            //Get calendar object with current date/time
            val defaultCalc = Calendar.getInstance(defaultTz)

            //Get offset from UTC, accounting for DST
            val defaultTzOffsetMs = defaultCalc.get(Calendar.ZONE_OFFSET) + defaultCalc.get(Calendar.DST_OFFSET)
            val timezone = (defaultTzOffsetMs / (1000 * 60 * 60)).toDouble()

            // Test Prayer times here
            val prayers = PrayTime()

            if (timeFormat == -1) {
                prayers.timeFormat = settings.getTimeFormatFor(index)
            } else {
                prayers.timeFormat = timeFormat
            }

            prayers.calcMethod = settings.getCalcMethodSetFor(index)
            prayers.asrJuristic = settings.getAsrMethodSetFor(index)
            prayers.adjustHighLats = settings.getHighLatitudeAdjustmentFor(index)

            val offsets = intArrayOf(0, 0, 0, 0, 0, 0, 0) // {Fajr,Sunrise,Dhuhr,Asr,Sunset,Maghrib,Isha}
            prayers.tune(offsets)

            val prayerTimes = prayers.getPrayerTimes(calendar, lat, lng, timezone)
            val prayerNames = prayers.timeNames
            val result = LinkedHashMap<String, String>()

            var preSuhoorTime = Calendar.getInstance(TimeZone.getDefault())
            preSuhoorTime.timeInMillis = System.currentTimeMillis()
            val time = (10 * 60 * 1000).toLong()
            preSuhoorTime = getCalendarFromPrayerTime(preSuhoorTime, prayerTimes[0])
            preSuhoorTime.timeInMillis = preSuhoorTime.timeInMillis - time + 2 * 60 * 1000

            val df = SimpleDateFormat("H:mm")
            val imsakTime = df.format(preSuhoorTime.time)

            println("Imsak - $imsakTime")
            result["Imsak"] = imsakTime

            for (i in prayerTimes.indices) {
                println(prayerNames[i] + " - " + prayerTimes[i])

                val t = prayerTimes[i].split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val hour = Integer.parseInt(t[0])
                val minute = Integer.parseInt(t[1])

                var totalMinute = 0

                if (prayerNames[i].toLowerCase() == "dhuhr")
                    totalMinute = hour * 60 + minute + 3
                else if (prayerNames[i].toLowerCase() == "sunrise")
                    totalMinute = hour * 60 + minute - 3
                else if (prayerNames[i].toLowerCase() == "maghrib") {
                    totalMinute = hour * 60 + minute + 4
                } else
                    totalMinute = hour * 60 + minute + 2

                val minuteLeft = totalMinute % 60
                val hourLeft = totalMinute / 60

                val customTime: String

                if (minuteLeft.toString().length < 2)
                    customTime = hourLeft.toString() + ":0" + minuteLeft
                else
                    customTime = hourLeft.toString() + ":" + minuteLeft


                println("Custom :" + prayerNames[i] + " - " + customTime)

                result[prayerNames[i]] = customTime
            }

            return result
        }
    }
}