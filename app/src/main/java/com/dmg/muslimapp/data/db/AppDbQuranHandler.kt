package com.dmg.muslimapp.data.db

import android.content.Context
import android.database.Cursor
import com.dmg.muslimapp.data.db.model.quran.Ayah
import kotlin.properties.Delegates

class AppDbQuranHandler(context: Context) {
    var mContext : Context by Delegates.notNull()
    var dbHandler: DbQuranHelper by Delegates.notNull()

    init {
        this.mContext = context
        this.dbHandler = DbQuranHelper(mContext)
        dbHandler.createDataBase()
    }

    fun getAyah(_id: Int): Ayah? {
        val query = "SELECT * FROM aya WHERE SuraID =  \"$_id\""
        val db = dbHandler.writableDatabase
        val cursor = db.rawQuery(query, null)
        var ayah: Ayah? = null

        if (cursor.moveToFirst()) {
            cursor.moveToFirst()

            val id = cursor.getInt(cursor.getColumnIndex("ID"))
            val sura_id = cursor.getInt(cursor.getColumnIndex("SuraID"))
            val verse_id = cursor.getInt(cursor.getColumnIndex("VerseID"))
            val ayah_text = cursor.getString(cursor.getColumnIndex("AyahText"))
            val translation = cursor.getString(cursor.getColumnIndex("AyahText"))
            ayah = Ayah(id, sura_id, verse_id, ayah_text, translation)
            cursor.close()
        }

        db.close()
        return ayah
    }

    fun getAllAyah(sid: Int): List<Ayah> {
        val db = dbHandler.writableDatabase
        val list = ArrayList<Ayah>()
        val cursor: Cursor
        cursor = db.rawQuery("SELECT a.*, b.translation_aya_text FROM aya a JOIN translation_aya b ON a.aya_id = b.aya_id WHERE sura_id=$sid ORDER BY a.aya_id", null)
        if (cursor != null) {
            if (cursor.count > 0) {
                cursor.moveToFirst()
                do {
                    val id = cursor.getInt(cursor.getColumnIndex("aya_id"))
                    val sura_id = cursor.getInt(cursor.getColumnIndex("sura_id"))
                    val verse_id = cursor.getInt(cursor.getColumnIndex("aya_number"))
                    val ayah_text = cursor.getString(cursor.getColumnIndex("aya_text"))
                    val translation = cursor.getString(cursor.getColumnIndex("translation_aya_text"))
                    val ayah = Ayah(id, sura_id, verse_id, ayah_text, translation)
                    list.add(ayah)
                } while (cursor.moveToNext())
            }
        }
        return list
    }

    fun getFeedAyah(): List<Ayah> {
        val getAnswerIds = arrayOf("1", "2", "98", "60", "5", "6", "7")
        var after_first = false
        val sb = StringBuilder()
        for (s in getAnswerIds) {
            if (after_first) {
                sb.append(",")
            } else {
                after_first = true
            }
            sb.append(s)
        }
        val ids = sb.toString()

        val db = dbHandler.writableDatabase
        val list = ArrayList<Ayah>()
        val cursor: Cursor
        cursor = db.rawQuery("SELECT a.*, b.translation_aya_text FROM aya a JOIN translation_aya b ON a.aya_id = b.aya_id WHERE a.aya_id in ($ids) ", null)
        if (cursor != null) {
            if (cursor.count > 0) {
                cursor.moveToFirst()
                do {
                    val id = cursor.getInt(cursor.getColumnIndex("aya_id"))
                    val sura_id = cursor.getInt(cursor.getColumnIndex("sura_id"))
                    val verse_id = cursor.getInt(cursor.getColumnIndex("aya_number"))
                    val ayah_text = cursor.getString(cursor.getColumnIndex("aya_text"))
                    val translation = cursor.getString(cursor.getColumnIndex("translation_aya_text"))
                    val ayah = Ayah(id, sura_id, verse_id, ayah_text, translation)
                    list.add(ayah)
                } while (cursor.moveToNext())
            }
        }
        return list
    }
}