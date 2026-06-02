package com.dmg.muslimapp.data.db.model.users

import com.dmg.muslimapp.utils.Constants

class UserLocation(
        var id: Int,
        var latitude: Double? = Constants.DEFAULT_BDG_LAT,
        var longitude: Double? = Constants.DEFAULT_BDG_LNG,
        var address: String?,
        var road: String?,
        var city: String?,
        var state: String?,
        var subState: String?,
        var country: String?,
        var postalCode: String?,
        var knownName: String?)