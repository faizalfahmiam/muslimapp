package com.dmg.muslimapp.utils

import android.content.Context
import android.widget.Toast

class ToastMessage {
    var msg: String

    constructor(msg: String, con: Context) {
        this.msg = msg
        con.toast(message = msg, toastDuration = Toast.LENGTH_LONG)
    }

    fun Context.toast(context: Context = applicationContext, message: String, toastDuration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(context, message, toastDuration).show()
    }
}