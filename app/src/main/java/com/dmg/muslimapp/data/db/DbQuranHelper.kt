package com.dmg.muslimapp.data.db

import android.annotation.SuppressLint
import android.content.Context
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

import com.dmg.muslimapp.BuildConfig

import java.io.File
import java.io.FileOutputStream
import java.io.IOException

open class DbQuranHelper(private val myContext: Context) : SQLiteOpenHelper(myContext, DATABASE_NAME, null, DATABASE_VERSION) {

    private var myDataBase: SQLiteDatabase? = null

    companion object {
        val DATABASE_NAME = "quran2.db"// "db.sqlite";
        @SuppressLint("SdCardPath")
        val DATABASE_PATH = "/data/data/" + BuildConfig.APPLICATION_ID + "/databases/"
        val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase) {
        // TODO Auto-generated method stub
        Log.e("onCreate", "create db")
    }

    // Create a empty database on the system
    @Throws(IOException::class)
    fun createDataBase() {

        val dbExist = checkDataBase()

        if (dbExist) {
            Log.v("DB Exists", "db exists")

        } else {

            this.readableDatabase
            try {
                this.close()
                copyDataBase()
            } catch (e: IOException) {
                throw Error("Error copying database")
            }

        }
    }

    // Check database already exist or not
    private fun checkDataBase(): Boolean {
        var checkDB = false
        try {
            val myPath = DATABASE_PATH + DATABASE_NAME
            val dbfile = File(myPath)
            checkDB = dbfile.exists()
        } catch (e: SQLiteException) {
            println("delete database file.")
        }

        return checkDB
    }

    // Copies your database from your local assets-folder to the just created
    // empty database in the system folder
    @Throws(IOException::class)
    private fun copyDataBase() {

        val outFileName = DATABASE_PATH + DATABASE_NAME

        val myOutput = FileOutputStream(outFileName)
        val myInput = myContext.assets.open("databases/" + DATABASE_NAME)

        val buffer = ByteArray(1024)
        var length: Int = myInput.read(buffer)
        while ((length) > 0) {
            myOutput.write(buffer, 0, length)
            length = myInput.read(buffer)
        }
        myInput.close()
        myOutput.flush()
        myOutput.close()
    }

    // Open database
    @Throws(SQLException::class)
    fun openDatabase() {
        val myPath = DATABASE_PATH + DATABASE_NAME
        myDataBase = SQLiteDatabase.openDatabase(myPath, null,
                SQLiteDatabase.OPEN_READWRITE)
    }

    @Synchronized
    @Throws(SQLException::class)
    fun closeDataBase() {
        if (myDataBase != null)
            myDataBase!!.close()
        super.close()
    }

    // delete database
    fun db_delete() {
        val file = File(DATABASE_PATH + DATABASE_NAME)
        if (file.exists()) {
            file.delete()
            println("delete database file.")
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (newVersion > oldVersion) {
            Log.v("Database Upgrade", "Database version higher than old.")
            db_delete()
        }
    }

}

