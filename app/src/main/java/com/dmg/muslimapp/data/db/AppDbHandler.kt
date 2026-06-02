package com.dmg.muslimapp.data.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.util.Log
import com.dmg.muslimapp.data.db.model.service_menu.ServiceMenu
import com.dmg.muslimapp.data.db.model.users.UserLocation
import com.dmg.muslimapp.data.db.model.users.UserProfile
import kotlin.properties.Delegates

class AppDbHandler(context: Context) {
    var mContext : Context by Delegates.notNull()
    private var dbHelper: DbHelper by Delegates.notNull()

    init {
        this.mContext = context
        this.dbHelper = DbHelper(mContext)
        //dbHandler.createDataBase()
    }

    fun getUserProfile(): UserProfile? {
        val query = "SELECT * FROM tbl_user WHERE _index = 0"
        val db = dbHelper.writableDatabase
        val cursor = db.rawQuery(query, null)
        var profile: UserProfile? = null

        if (cursor.moveToFirst()) {
            cursor.moveToFirst()

            val index = cursor.getInt(cursor.getColumnIndex(DbHelper.KEY_INDEX))
            val id = cursor.getInt(cursor.getColumnIndex(DbHelper.KEY_USERID))
            val nama = cursor.getString(cursor.getColumnIndex(DbHelper.KEY_NAME))
            val noHp = cursor.getString(cursor.getColumnIndex(DbHelper.KEY_NO_HP))
            val email = cursor.getString(cursor.getColumnIndex(DbHelper.KEY_EMAIL))
            val createdAt = cursor.getString(cursor.getColumnIndex(DbHelper.KEY_CREATE_AT))
            val updateAt = cursor.getString(cursor.getColumnIndex(DbHelper.KEY_UPDATE_AT))
            val profilePicture = cursor.getString(cursor.getColumnIndex(DbHelper.KEY_PROFILE_PICTURE))
            val tglLahir = cursor.getString(cursor.getColumnIndex(DbHelper.KEY_TGL_LAHIR))
            val alamat = cursor.getString(cursor.getColumnIndex(DbHelper.KEY_ALAMAT))
            profile = UserProfile(index, id, nama, noHp, email, createdAt, updateAt, profilePicture, tglLahir, alamat)
            cursor.close()
        }

        db.close()
        return profile
    }

    fun getUserLocation(): UserLocation? {
        val query = "SELECT * FROM ${DbHelper.TABLE_USERS_LOCATION} WHERE ${DbHelper.KEY_ID}=0"
        val db = dbHelper.writableDatabase
        val cursor = db.rawQuery(query, null)
        var location: UserLocation? = null

        if (cursor.moveToFirst()) {
            cursor.moveToFirst()

            val id = cursor.getInt(cursor.getColumnIndex(DbHelper.KEY_ID))
            val latitude = cursor.getDouble(cursor.getColumnIndex(DbHelper.KEY_LAT))
            val longitude = cursor.getDouble(cursor.getColumnIndex(DbHelper.KEY_LNG))
            val address = cursor.getString(cursor.getColumnIndex(DbHelper.KEY_ADDRESS))
            val road = cursor.getString(cursor.getColumnIndex(DbHelper.KEY_ROAD))
            val city = cursor.getString(cursor.getColumnIndex(DbHelper.KEY_CITY))
            val state = cursor.getString(cursor.getColumnIndex(DbHelper.KEY_STATE))
            val subState = cursor.getString(cursor.getColumnIndex(DbHelper.KEY_SUB_STATE))
            val country = cursor.getString(cursor.getColumnIndex(DbHelper.KEY_COUNTRY))
            val postalCode = cursor.getString(cursor.getColumnIndex(DbHelper.KEY_POSTAL_CODE))
            val knownName = cursor.getString(cursor.getColumnIndex(DbHelper.KEY_KNOWNAME))
            location = UserLocation(id, latitude, longitude, address, road, city, state, subState, country, postalCode, knownName)
            cursor.close()
        }

        db.close()
        return location
    }

    fun addOrUpdateUserLocation(location: UserLocation): Boolean {
        // The database connection is cached so it's not expensive to call getWriteableDatabase() multiple times.

        val db = dbHelper.writableDatabase
        var response = false

        try {
            if (db.rawQuery("SELECT * FROM ${DbHelper.TABLE_USERS_LOCATION} WHERE ${DbHelper.KEY_ID}=0", null).count == 0) {
                val values = ContentValues()
                values.put(DbHelper.KEY_ID, 0)
                values.put(DbHelper.KEY_LAT, location.latitude)
                values.put(DbHelper.KEY_LNG, location.longitude)
                values.put(DbHelper.KEY_ADDRESS, location.address)
                values.put(DbHelper.KEY_ROAD, location.road)
                values.put(DbHelper.KEY_CITY, location.city)
                values.put(DbHelper.KEY_STATE, location.state)
                values.put(DbHelper.KEY_SUB_STATE, location.subState)
                values.put(DbHelper.KEY_COUNTRY, location.country)
                values.put(DbHelper.KEY_POSTAL_CODE, location.postalCode)
                values.put(DbHelper.KEY_KNOWNAME, location.knownName)
                val _success =  db.insert(DbHelper.TABLE_USERS_LOCATION, null, values)
                response = (Integer.parseInt("$_success") != -1)
                Log.e("INSERT db", response.toString())
            } else {
                val values = ContentValues()
                values.put(DbHelper.KEY_LAT, location.latitude)
                values.put(DbHelper.KEY_LNG, location.longitude)
                values.put(DbHelper.KEY_ADDRESS, location.address)
                values.put(DbHelper.KEY_ROAD, location.road)
                values.put(DbHelper.KEY_CITY, location.city)
                values.put(DbHelper.KEY_STATE, location.state)
                values.put(DbHelper.KEY_SUB_STATE, location.subState)
                values.put(DbHelper.KEY_COUNTRY, location.country)
                values.put(DbHelper.KEY_POSTAL_CODE, location.postalCode)
                values.put(DbHelper.KEY_KNOWNAME, location.knownName)
                db!!.update(DbHelper.TABLE_USERS_LOCATION, values, DbHelper.KEY_ID + " = 0", null)
                response = true
                Log.e("UPDATE db", response.toString())
            }

        } catch (e: Exception) {
            Log.e("Error Save Account", e.toString())
            response = false
        }

        return response
    }

    fun addOrUpdateUserProfile(user: UserProfile): Boolean {
        // The database connection is cached so it's not expensive to call getWriteableDatabase() multiple times.

        val db = dbHelper.writableDatabase
        var response = false

        try {
            if (db.rawQuery("SELECT * FROM ${DbHelper.TABLE_USERS} WHERE ${DbHelper.KEY_INDEX}=0", null).count == 0) {
                val values = ContentValues()
                values.put(DbHelper.KEY_INDEX, 0)
                values.put(DbHelper.KEY_USERID, user.userId)
                values.put(DbHelper.KEY_NAME, user.nama)
                values.put(DbHelper.KEY_NO_HP, user.noHp)
                values.put(DbHelper.KEY_EMAIL, user.email)
                values.put(DbHelper.KEY_CREATE_AT, user.createdAt)
                values.put(DbHelper.KEY_UPDATE_AT, user.updateAt)
                values.put(DbHelper.KEY_PROFILE_PICTURE, user.updateAt)
                values.put(DbHelper.KEY_TGL_LAHIR, user.tglLahir)
                values.put(DbHelper.KEY_ALAMAT, user.alamat)
                val _success =  db.insert(DbHelper.TABLE_USERS, null, values)
                response = (Integer.parseInt("$_success") != -1)
                Log.e("INSERT db", response.toString())
            } else {
                val values = ContentValues()
                values.put(DbHelper.KEY_USERID, user.userId)
                values.put(DbHelper.KEY_NAME, user.nama)
                values.put(DbHelper.KEY_NO_HP, user.noHp)
                values.put(DbHelper.KEY_EMAIL, user.email)
                values.put(DbHelper.KEY_CREATE_AT, user.createdAt)
                values.put(DbHelper.KEY_UPDATE_AT, user.updateAt)
                values.put(DbHelper.KEY_PROFILE_PICTURE, user.updateAt)
                values.put(DbHelper.KEY_TGL_LAHIR, user.tglLahir)
                values.put(DbHelper.KEY_ALAMAT, user.alamat)
                db!!.update(DbHelper.TABLE_USERS, values, DbHelper.KEY_INDEX + " = 0", null)
                response = true
                Log.e("UPDATE db", response.toString())
            }

        } catch (e: Exception) {
            Log.e("Error Save Account", e.toString())
            response = false
        }

        return response
    }

    fun addOrUpdateServiceMenu(menu: ServiceMenu): Boolean {
        // The database connection is cached so it's not expensive to call getWriteableDatabase() multiple times.

        val db = dbHelper.writableDatabase
        var response = false

        try {
            if (db.rawQuery("SELECT * FROM ${DbHelper.TABLE_MENU_SERVICE} WHERE ${DbHelper.KEY_MENU_INDEX} = ${menu.menu_index}", null).count == 0) {
                val values = ContentValues()
                values.put(DbHelper.KEY_MENU_INDEX, menu.menu_index)
                values.put(DbHelper.KEY_MENU_ID, menu.menu_id)
                values.put(DbHelper.KEY_MENU_STATUS, menu.menu_status)
                val _success =  db.insert(DbHelper.TABLE_MENU_SERVICE, null, values)
                response = (Integer.parseInt("$_success") != -1)
                Log.e("INSERT db", response.toString())
            } else {
                val values = ContentValues()
                values.put(DbHelper.KEY_MENU_ID, menu.menu_id)
                values.put(DbHelper.KEY_MENU_STATUS, menu.menu_status)
                db!!.update(DbHelper.TABLE_MENU_SERVICE, values, DbHelper.KEY_MENU_INDEX + " = " +menu.menu_index, null)
                response = true
                Log.e("UPDATE db", response.toString())
            }

        } catch (e: Exception) {
            Log.e("Error Save Service Menu", e.toString())
            response = false
        }

        return response
    }

    fun getServiceMenu(): ServiceMenu? {
        val query = "SELECT * FROM tbl_service_menu WHERE menu_index = 0"
        val db = dbHelper.writableDatabase
        val cursor = db.rawQuery(query, null)
        var menu: ServiceMenu? = null

        if (cursor.moveToFirst()) {
            cursor.moveToFirst()

            val index = cursor.getInt(cursor.getColumnIndex(DbHelper.KEY_MENU_INDEX))
            val id = cursor.getString(cursor.getColumnIndex(DbHelper.KEY_MENU_ID))
            val status: Boolean = cursor.getInt(cursor.getColumnIndex(DbHelper.KEY_MENU_STATUS)) > 0
            menu = ServiceMenu(index, id, status)
            cursor.close()
        }

        db.close()
        return menu
    }

    fun getServiceMenuList(): List<ServiceMenu> {
        val db = dbHelper.writableDatabase
        val list = ArrayList<ServiceMenu>()
        val cursor: Cursor
        cursor = db.rawQuery("SELECT * FROM tbl_service_menu WHERE menu_status = 1 ORDER BY menu_index", null)
        if (cursor != null) {
            if (cursor.count > 0) {
                cursor.moveToFirst()
                do {
                    val index = cursor.getInt(cursor.getColumnIndex(DbHelper.KEY_MENU_INDEX))
                    val id = cursor.getString(cursor.getColumnIndex(DbHelper.KEY_MENU_ID))
                    val status: Boolean = cursor.getInt(cursor.getColumnIndex(DbHelper.KEY_MENU_STATUS)) > 0
                    val user = ServiceMenu(index, id, status)
                    list.add(user)
                } while (cursor.moveToNext())
            }
        }
        return list
    }
}