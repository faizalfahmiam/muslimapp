package com.dmg.muslimapp.data.network.model.masjid

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class MasjidNearbyList(
        @Expose
        @SerializedName("id")
        var id: String,

        @Expose
        @SerializedName("nama")
        var nama: String
): Serializable