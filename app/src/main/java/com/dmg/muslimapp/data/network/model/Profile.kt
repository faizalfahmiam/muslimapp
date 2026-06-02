package com.dmg.muslimapp.data.network.model

import com.dmg.muslimapp.data.db.model.users.UserProfile
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Profile(
        @Expose
        @SerializedName("ref")
        var ref: String,

        @Expose
        @SerializedName("data")
        var data: UserProfile
)