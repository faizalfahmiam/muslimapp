package com.dmg.muslimapp.data.db

import android.content.Context
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

import com.dmg.muslimapp.BuildConfig

import java.io.File
import java.io.IOException

open class DbHelper(private val myContext: Context) : SQLiteOpenHelper(myContext, DATABASE_NAME, null, DATABASE_VERSION) {

    private lateinit var myDataBase: SQLiteDatabase

    companion object {
        const val DATABASE_NAME = "muslimapp.db"// "db.sqlite";
        const val DATABASE_PATH = "/data/data/" + BuildConfig.APPLICATION_ID + "/databases/"
        const val DATABASE_VERSION = 1

        // Table Name
        val TABLE_USERS = "tbl_user"
        val KEY_INDEX = "_index"
        val KEY_USERID = "user_id"
        val KEY_NAME = "nama"
        val KEY_NO_HP = "no_hp"
        val KEY_EMAIL = "email"
        val KEY_CREATE_AT = "create_at"
        val KEY_UPDATE_AT = "update_at"
        val KEY_PROFILE_PICTURE = "profile_picture"
        val KEY_TGL_LAHIR = "tgl_lahir"
        val KEY_ALAMAT = "alamat"

        val TABLE_USERS_LOCATION = "tbl_user_location"
        val KEY_ID = "_id"
        val KEY_LAT = "latitude"
        val KEY_LNG = "longitude"
        val KEY_ADDRESS = "address"
        val KEY_ROAD = "road"
        val KEY_CITY = "city"
        val KEY_STATE = "state"
        val KEY_SUB_STATE = "sub_state"
        val KEY_COUNTRY = "country"
        val KEY_POSTAL_CODE = "postal_code"
        val KEY_KNOWNAME = "knowname"

        val TABLE_MENU_SERVICE = "tbl_service_menu"
        val KEY_MENU_INDEX = "menu_index"
        val KEY_MENU_ID = "menu_id"
        val KEY_MENU_STATUS = "menu_status"

    }

    override fun onCreate(db: SQLiteDatabase) {
        Log.e("onCreate", "create db")
        createTableUser(db)
        createTableUserLocation(db)
        createTableServiceMenu(db)
    }

    // Create a empty database on the system
    @Throws(IOException::class)
    fun createDataBase(db: SQLiteDatabase) {

        val dbExist = checkDataBase()

        if (dbExist) {
            Log.e("DB Exists", "db exists")

        } else {
            this.readableDatabase

            Log.e("onCreate", "table db")
            try {
                this.close()

                Log.e("onCreate", "table db 2")
                createTableUser(db)
                createTableUserLocation(db)
                createTableServiceMenu(db)
            } catch (e: IOException) {
                throw Error("Error copying database")
            }

        }
    }

    // Update Database
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (newVersion > oldVersion) {
            Log.v("Database Upgrade", "Database version higher than old.")
            //db_delete()
        }
    }

    private fun createTableUser(db: SQLiteDatabase) {
        val CREATE_USERS_TABLE = """CREATE TABLE $TABLE_USERS(
                $KEY_INDEX INTEGER PRIMARY KEY,
                $KEY_USERID TEXT,
                $KEY_NAME TEXT,
                $KEY_NO_HP TEXT,
                $KEY_EMAIL TEXT,
                $KEY_CREATE_AT TEXT,
                $KEY_UPDATE_AT TEXT,
                $KEY_PROFILE_PICTURE TEXT,
                $KEY_TGL_LAHIR TEXT,
                $KEY_ALAMAT TEXT)"""

        db.execSQL(CREATE_USERS_TABLE)
    }

    private fun createTableUserLocation(db: SQLiteDatabase) {
        val CREATE_USERS_TABLE = """CREATE TABLE $TABLE_USERS_LOCATION(
                $KEY_ID INTEGER PRIMARY KEY,
                $KEY_LAT DOUBLE,
                $KEY_LNG DOUBLE,
                $KEY_ADDRESS TEXT,
                $KEY_ROAD TEXT,
                $KEY_CITY TEXT,
                $KEY_STATE TEXT,
                $KEY_SUB_STATE TEXT,
                $KEY_COUNTRY TEXT,
                $KEY_POSTAL_CODE TEXT,
                $KEY_KNOWNAME TEXT)"""

        db.execSQL(CREATE_USERS_TABLE)
    }

    private fun createTableServiceMenu(db: SQLiteDatabase) {
        val CREATE_SERVICE_MENU = """CREATE TABLE $TABLE_MENU_SERVICE(
                $KEY_MENU_INDEX INTEGER PRIMARY KEY,
                $KEY_MENU_ID TEXT,
                $KEY_MENU_STATUS BOOLEAN)"""

        db.execSQL(CREATE_SERVICE_MENU)
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
    private fun db_delete() {
        val file = File(DATABASE_PATH + DATABASE_NAME)
        if (file.exists()) {
            file.delete()
            println("delete database file.")
        }
    }

}

