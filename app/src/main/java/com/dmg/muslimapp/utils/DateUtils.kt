package com.dmg.muslimapp.utils

import android.content.Context
import android.util.Log
import com.dmg.muslimapp.R
import com.franmontiel.localechanger.LocaleChanger
import java.text.DateFormat
import java.text.DateFormatSymbols
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

import com.github.msarhan.ummalqura.calendar.UmmalquraCalendar

object DateUtils {

    private val TAG = DateUtils::class.java.simpleName

    internal lateinit var calendar: Calendar
    internal lateinit var monthNameFormat: DateFormat
    internal lateinit var monthShortNameFormat: DateFormat
    internal lateinit var weekdayNameFormat: DateFormat
    internal lateinit var weekdayShortNameFormat: DateFormat

    val currentDate: Date
        get() = Calendar.getInstance().time

    /** The maximum date possible.  */
    var MAX_DATE = Date(java.lang.Long.MAX_VALUE)

    fun getStringDate(date: Date): String {
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        return simpleDateFormat.format(date)
    }

    fun getCurrentDateHijri(days: Int, locale: String): String {
        val cal = UmmalquraCalendar()
        cal.add(Calendar.DATE, days)
        cal.get(Calendar.YEAR)         // 1436
        cal.get(Calendar.MONTH)        // 5 <=> Jumada al-Akhirah
        cal.get(Calendar.DAY_OF_MONTH) // 14

        val dateFormat = SimpleDateFormat("", Locale.ENGLISH)
        dateFormat.calendar = cal

        dateFormat.applyPattern("M")

        var bulan: Array<String>? = null
        bulan = arrayOf("Muharram", "Safar", "Rabiul awal", "Rabiul akhir", "Jumadil awal", "Jumadil akhir", "Rajab", "Sya'ban", "Ramadhan", "Syawal", "Dzulkaidah", "Dzulhijjah")
        if (locale == "en") {
            bulan = arrayOf("Muharram", "Safar", "Rabiul early", "Final Rabiul", "Jumadil early", "Final Jumadil", "Rajab", "Sya'ban", "Ramadhan", "Syawal", "Dzulkaidah", "Dzulhijjah")
        } else if (locale == "ms") {
            bulan = arrayOf("Muharram", "Safar", "Rabiul awal", "Rabiul akhir", "Jumadil awal", "Jumadil akhir", "Rajab", "Sya'ban", "Ramadhan", "Syawal", "Dzulkaidah", "Dzulhijjah")
        } else if (locale == "ar") {
            bulan = arrayOf("مقدس", "صفر", "ربيع في وقت مبكر", "رابع النهائي", "في وقت مبكر الجمعة", "الجمعة الماضية", "رجب", "شعبان", "رمضان", "فراق", "ذو القعدة", "ذو الحجة")
        }

        //String sBulan = bulan[Integer.parseInt(dateFormat.format(cal.getTime())) - 1];

        Log.e("Mont Hijri : ", cal.getTime().toString() +  " FORMAT : " + dateFormat.format(cal.getTime()))
        val b = Integer.parseInt(dateFormat.format(cal.getTime())) - 1
        Log.e("Mont Hijri Array Ke :", "$b")
        val sBulan = bulan[b]

        dateFormat.applyPattern("dd")
        val tanggal = dateFormat.format(cal.getTime())
        dateFormat.applyPattern("y")
        val tahun = dateFormat.format(cal.getTime())

        Log.d("Tanggal : ", "$tanggal $sBulan $tahun H")
        return "$tanggal $sBulan $tahun H"
    }

    fun getCurrentDateMasehi(context: Context, days: Int): String {
        val calendar = Calendar.getInstance(TimeZone.getDefault())
        calendar.add(Calendar.DATE, days)
        return DateUtils.getStringDate(context.resources.getString(R.string.patern_dd_mmmm_yyyy), calendar.time)
    }

    fun getTimeleft(time: String): String {
        try {
            val t = time.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val hour = Integer.parseInt(t[0])
            val minute = Integer.parseInt(t[1])
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)
            val timeAdzan = calendar.timeInMillis
            val timeNow = Calendar.getInstance(TimeZone.getDefault()).timeInMillis
            val diff = timeAdzan - timeNow

            if (diff < 0) {
                return ""
            } else {
                val totalMinute = diff / (1000 * 60)
                val minuteLeft = totalMinute % 60
                val hourLeft = totalMinute / 60
                return hourLeft.toString() + ":" + minuteLeft
            }

        } catch (e: Exception) {
         //   AppLogger.d("getTimeLeft Exception " + e.message)
            return ""
        }

        //        SimpleDateFormat df = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        //        Calendar calendar = Calendar.getInstance();
        //        calendar.set(Calendar.HOUR_OF_DAY);
        //        String formattedDate = df.format(getCurrentDate());
        //        try {
        //            Date now = df.parse(formattedDate);
        //            SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        //            Date date2 = format.parse(time);
        //            String remaining1 = android.text.format.DateUtils.formatElapsedTime((date2.getTime() - now.getTime()) / 1000);
        //            if (!remaining1.subSequence(0, 2).equals("00")) {
        //                AppLogger.i(">>>>>>>"+ remaining1);
        //                SimpleDateFormat df1 = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        //                Date convertedDate = df1.parse(remaining1);
        //                String formattedDate1 = df.format(convertedDate);
        //                return formattedDate1;
        //            }
        //            else {
        //                return "";
        //            }
        //        } catch (ParseException e) {
        //            e.printStackTrace();
        //            return "";
        //        }
    }

    fun getDateFormatIndonesia(tgl: String, format: String): String {
        val date = parseStringDate(tgl, "yyyy-MM-dd HH:mm:ss")

        var hasil = ""

        if (format == "dd MMMM yyyy")
            hasil = DateUtils.getStringDate("dd", date) + " " + DateUtils.monthNameFormat().format(date) + " " + DateUtils.getStringDate("yyyy", date)
        else if (format == "dd MMM yyyy")
            hasil = DateUtils.getStringDate("dd", date) + " " + DateUtils.monthShortNameFormat().format(date) + " " + DateUtils.getStringDate("yyyy", date)
        else if (format == "dd MMM")
            hasil = DateUtils.getStringDate("dd", date) + " " + DateUtils.monthShortNameFormat().format(date)
        else if (format == "dd")
            hasil = DateUtils.getStringDate("dd", date)
        else if (format == "MMM")
            hasil = DateUtils.monthShortNameFormat().format(date)
        else if (format == "yyyy")
            hasil = DateUtils.getStringDate("yyyy", date)

        return hasil
    }

    fun getStringDate(format: String, date: Date): String {
        val simpleDateFormat = SimpleDateFormat(format, Locale.ENGLISH)

        return simpleDateFormat.format(date)
    }

    fun getStringDate(format: String, date: Long): String {
        val simpleDateFormat = SimpleDateFormat(format, Locale.ENGLISH)

        return simpleDateFormat.format(DateUtils.longToDate(date))
    }

    fun getStringDate(format: String, date: String): String {
        val simpleDateFormat = SimpleDateFormat(format, Locale.ENGLISH)
        return simpleDateFormat.format(DateUtils.parseStringDate(date, "dd-MM-yyyy"))
    }


    fun getStringDate2(format: String, date: Long): String {
        val simpleDateFormat = SimpleDateFormat(format, Locale.ENGLISH)

        return simpleDateFormat.format(DateUtils.longToDate2(date))
    }

    fun parseStringDate(strDate: String, format: String): Date {
        try {
            val dateFormat = SimpleDateFormat(format, Locale.ENGLISH)

            return dateFormat.parse(strDate)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return today()
    }

    fun monthNameFormat(): DateFormat {
        return monthNameFormat
    }

    fun monthShortNameFormat(): DateFormat {
        return monthShortNameFormat
    }

    fun weekdayNameFormat(): DateFormat {
        return weekdayNameFormat
    }

    fun weekdayShortNameFormat(): DateFormat {
        return weekdayShortNameFormat
    }

    @JvmStatic
    fun setMonthsName(newMonths: Array<String>) {
        val symbols = DateFormatSymbols(Locale.ENGLISH)
        symbols.months = newMonths
        monthNameFormat = SimpleDateFormat("MMMM", symbols)
    }

    @JvmStatic
    fun setShortMonthsName(newShortMonths: Array<String>) {
        val symbols = DateFormatSymbols(Locale.ENGLISH)
        symbols.shortMonths = newShortMonths
        monthShortNameFormat = SimpleDateFormat("MMM", symbols)
    }

    @JvmStatic
    fun setWeekdaysName(newWeekdays: Array<String>) {
        val symbols = DateFormatSymbols(Locale.ENGLISH)
        symbols.weekdays = newWeekdays
        weekdayNameFormat = SimpleDateFormat("EEEE", symbols)
    }

    @JvmStatic
    fun setShortWeekdaysName(newShortWeekdays: Array<String>) {
        val symbols = DateFormatSymbols(Locale.ENGLISH)
        symbols.shortWeekdays = newShortWeekdays
        weekdayShortNameFormat = SimpleDateFormat("EEE", symbols)
    }

    fun longToDate(`val`: Long): Date {
        return Date(`val` * 1000)
    }

    fun longToDate2(`val`: Long): Date {
        return Date(`val`)
    }

    fun dateToLong(`val`: Date): Long {
        return `val`.time / 1000
    }

    fun today(): Date {
        calendar = Calendar.getInstance()
        return calendar.time
    }

    fun tomorrow(): Date {
        calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        return calendar.time
    }

    fun nextYear(): Date {
        calendar = Calendar.getInstance()
        calendar.add(Calendar.YEAR, 1)
        return calendar.time
    }

    fun nextMonth(): Date {
        calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, 1)
        return calendar.time
    }

    fun monthLabels(): Array<String> {

        return arrayOf("Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember")
    }

    fun getmonthLabel(posisi: Int): String {

        val titles = arrayOf("Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember")

        return titles[posisi]
    }

    fun monthLabelsShort(): Array<String> {

        return arrayOf("Jan", "Feb", "Mar", "Apr", "Mei", "Jun", "Jul", "Agu", "Sep", "Okt", "Nov", "Des")
    }

    fun getmonthLabelShort(posisi: Int): String {

        val titles = arrayOf("Jan", "Feb", "Mar", "Apr", "Mei", "Jun", "Jul", "Agu", "Sep", "Okt", "Nov", "Des")

        return titles[posisi]
    }

    fun weekDayLabels(): Array<String> {

        return arrayOf("#", "Minggu", "Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu")
    }

    fun weekDayLabelsShort(): Array<String> {

        return arrayOf("#", "Min", "Sen", "Sel", "Rab", "Kam", "Jum", "Sab")
    }

    fun getExpiringSubscriptionDate(purchaseDate: Date): String {
        return getExpiringSubscriptionDate("dd MMM yyyy", purchaseDate)
    }

    fun getExpiringSubscriptionDate(format: String, purchaseDate: Date): String {
        //        Date currentDate = new Date();
        //        currentDate.setTime(Calendar.getInstance().getTimeInMillis());
        val calendarNow = Calendar.getInstance()
        val calendarExpiryDate = Calendar.getInstance()
        calendarExpiryDate.time = purchaseDate
        while (calendarNow.timeInMillis > calendarExpiryDate.timeInMillis) {
            calendarExpiryDate.add(Calendar.MONTH, 1)
        }
        val simpleDateFormat = SimpleDateFormat(format, LocaleChanger.getLocale())
        return simpleDateFormat.format(calendarExpiryDate.time)
    }

    fun isSameDay(date1: Date?, date2: Date?): Boolean {
        if (date1 == null || date2 == null) {
            throw IllegalArgumentException("The dates must not be null")
        }
        val cal1 = Calendar.getInstance()
        cal1.time = date1
        val cal2 = Calendar.getInstance()
        cal2.time = date2
        return isSameDay(cal1, cal2)
    }

    /**
     *
     * Checks if two calendars represent the same day ignoring time.
     * @param cal1  the first calendar, not altered, not null
     * @param cal2  the second calendar, not altered, not null
     * @return true if they represent the same day
     * @throws IllegalArgumentException if either calendar is `null`
     */
    fun isSameDay(cal1: Calendar?, cal2: Calendar?): Boolean {
        if (cal1 == null || cal2 == null) {
            throw IllegalArgumentException("The dates must not be null")
        }
        return cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
    }

    /**
     *
     * Checks if a date is today.
     * @param date the date, not altered, not null.
     * @return true if the date is today.
     * @throws IllegalArgumentException if the date is `null`
     */
    fun isToday(date: Date): Boolean {
        return isSameDay(date, Calendar.getInstance().time)
    }

    /**
     *
     * Checks if a calendar date is today.
     * @param cal  the calendar, not altered, not null
     * @return true if cal date is today
     * @throws IllegalArgumentException if the calendar is `null`
     */
    fun isToday(cal: Calendar): Boolean {
        return isSameDay(cal, Calendar.getInstance())
    }

    /**
     *
     * Checks if the first date is before the second date ignoring time.
     * @param date1 the first date, not altered, not null
     * @param date2 the second date, not altered, not null
     * @return true if the first date day is before the second date day.
     * @throws IllegalArgumentException if the date is `null`
     */
    fun isBeforeDay(date1: Date?, date2: Date?): Boolean {
        if (date1 == null || date2 == null) {
            throw IllegalArgumentException("The dates must not be null")
        }
        val cal1 = Calendar.getInstance()
        cal1.time = date1
        val cal2 = Calendar.getInstance()
        cal2.time = date2
        return isBeforeDay(cal1, cal2)
    }

    /**
     *
     * Checks if the first calendar date is before the second calendar date ignoring time.
     * @param cal1 the first calendar, not altered, not null.
     * @param cal2 the second calendar, not altered, not null.
     * @return true if cal1 date is before cal2 date ignoring time.
     * @throws IllegalArgumentException if either of the calendars are `null`
     */
    fun isBeforeDay(cal1: Calendar?, cal2: Calendar?): Boolean {
        if (cal1 == null || cal2 == null) {
            throw IllegalArgumentException("The dates must not be null")
        }
        if (cal1.get(Calendar.ERA) < cal2.get(Calendar.ERA)) return true
        if (cal1.get(Calendar.ERA) > cal2.get(Calendar.ERA)) return false
        if (cal1.get(Calendar.YEAR) < cal2.get(Calendar.YEAR)) return true
        return if (cal1.get(Calendar.YEAR) > cal2.get(Calendar.YEAR)) false else cal1.get(Calendar.DAY_OF_YEAR) < cal2.get(Calendar.DAY_OF_YEAR)
    }

    /**
     *
     * Checks if the first date is after the second date ignoring time.
     * @param date1 the first date, not altered, not null
     * @param date2 the second date, not altered, not null
     * @return true if the first date day is after the second date day.
     * @throws IllegalArgumentException if the date is `null`
     */
    fun isAfterDay(date1: Date?, date2: Date?): Boolean {
        if (date1 == null || date2 == null) {
            throw IllegalArgumentException("The dates must not be null")
        }
        val cal1 = Calendar.getInstance()
        cal1.time = date1
        val cal2 = Calendar.getInstance()
        cal2.time = date2
        return isAfterDay(cal1, cal2)
    }

    /**
     *
     * Checks if the first calendar date is after the second calendar date ignoring time.
     * @param cal1 the first calendar, not altered, not null.
     * @param cal2 the second calendar, not altered, not null.
     * @return true if cal1 date is after cal2 date ignoring time.
     * @throws IllegalArgumentException if either of the calendars are `null`
     */
    fun isAfterDay(cal1: Calendar?, cal2: Calendar?): Boolean {
        if (cal1 == null || cal2 == null) {
            throw IllegalArgumentException("The dates must not be null")
        }
        if (cal1.get(Calendar.ERA) < cal2.get(Calendar.ERA)) return false
        if (cal1.get(Calendar.ERA) > cal2.get(Calendar.ERA)) return true
        if (cal1.get(Calendar.YEAR) < cal2.get(Calendar.YEAR)) return false
        return if (cal1.get(Calendar.YEAR) > cal2.get(Calendar.YEAR)) true else cal1.get(Calendar.DAY_OF_YEAR) > cal2.get(Calendar.DAY_OF_YEAR)
    }

    /**
     *
     * Checks if a date is after today and within a number of days in the future.
     * @param date the date to check, not altered, not null.
     * @param days the number of days.
     * @return true if the date day is after today and within days in the future .
     * @throws IllegalArgumentException if the date is `null`
     */
    fun isWithinDaysFuture(date: Date?, days: Int): Boolean {
        if (date == null) {
            throw IllegalArgumentException("The date must not be null")
        }
        val cal = Calendar.getInstance()
        cal.time = date
        return isWithinDaysFuture(cal, days)
    }

    /**
     *
     * Checks if a calendar date is after today and within a number of days in the future.
     * @param cal the calendar, not altered, not null
     * @param days the number of days.
     * @return true if the calendar date day is after today and within days in the future .
     * @throws IllegalArgumentException if the calendar is `null`
     */
    fun isWithinDaysFuture(cal: Calendar?, days: Int): Boolean {
        if (cal == null) {
            throw IllegalArgumentException("The date must not be null")
        }
        val today = Calendar.getInstance()
        val future = Calendar.getInstance()
        future.add(Calendar.DAY_OF_YEAR, days)
        return isAfterDay(cal, today) && !isAfterDay(cal, future)
    }

    /** Returns the given date with the time set to the start of the day.  */
    fun getStart(date: Date): Date? {
        return clearTime(date)
    }

    /** Returns the given date with the time values cleared.  */
    fun clearTime(date: Date?): Date? {
        if (date == null) {
            return null
        }
        val c = Calendar.getInstance()
        c.time = date
        c.set(Calendar.HOUR_OF_DAY, 0)
        c.set(Calendar.MINUTE, 0)
        c.set(Calendar.SECOND, 0)
        c.set(Calendar.MILLISECOND, 0)
        return c.time
    }

    /** Determines whether or not a date has any time values (hour, minute,
     * seconds or millisecondsReturns the given date with the time values cleared.  */

    /**
     * Determines whether or not a date has any time values.
     * @param date The date.
     * @return true iff the date is not null and any of the date's hour, minute,
     * seconds or millisecond values are greater than zero.
     */
    fun hasTime(date: Date?): Boolean {
        if (date == null) {
            return false
        }
        val c = Calendar.getInstance()
        c.time = date
        if (c.get(Calendar.HOUR_OF_DAY) > 0) {
            return true
        }
        if (c.get(Calendar.MINUTE) > 0) {
            return true
        }
        if (c.get(Calendar.SECOND) > 0) {
            return true
        }
        return if (c.get(Calendar.MILLISECOND) > 0) {
            true
        } else false
    }

    /** Returns the given date with time set to the end of the day  */
    fun getEnd(date: Date?): Date? {
        if (date == null) {
            return null
        }
        val c = Calendar.getInstance()
        c.time = date
        c.set(Calendar.HOUR_OF_DAY, 23)
        c.set(Calendar.MINUTE, 59)
        c.set(Calendar.SECOND, 59)
        c.set(Calendar.MILLISECOND, 999)
        return c.time
    }

    /**
     * Returns the maximum of two dates. A null date is treated as being less
     * than any non-null date.
     */
    fun max(d1: Date?, d2: Date?): Date? {
        if (d1 == null && d2 == null) return null
        if (d1 == null) return d2
        if (d2 == null) return d1
        return if (d1.after(d2)) d1 else d2
    }

    /**
     * Returns the minimum of two dates. A null date is treated as being greater
     * than any non-null date.
     */
    fun min(d1: Date?, d2: Date?): Date? {
        if (d1 == null && d2 == null) return null
        if (d1 == null) return d2
        if (d2 == null) return d1
        return if (d1.before(d2)) d1 else d2
    }


    fun FromISOFormat(format: String, dateValue: String): String? {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val outputFormat = SimpleDateFormat(format, Locale.ENGLISH)
        // use UTC as timezone
        //sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        try {
            val date = sdf.parse(dateValue)
            //outputFormat.setTimeZone(TimeZone.getTimeZone("Asia/Jakarta"));
            return outputFormat.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
            return null
        }

    }
}