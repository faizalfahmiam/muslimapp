package com.dmg.muslimapp.data.model

class Adzan (
        var name: String? = null,
        var time: String? = null,
        var image: Int = 0,
        var notif: Int = 0, // 0 = non active, 1 = sound, 2 = vibrate
        var status: Boolean = false,
        var custom: Boolean = false
)
