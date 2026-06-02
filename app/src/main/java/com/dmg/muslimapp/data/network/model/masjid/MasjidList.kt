package com.dmg.muslimapp.data.network.model.masjid

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class MasjidList(
        @Expose
        @SerializedName("id")
        var id: String,

        @Expose
        @SerializedName("nama_tempat")
        var nama_tempat: String,

        @Expose
        @SerializedName("jenis")
        var jenis: String,

        @Expose
        @SerializedName("lat")
        var lat: String,

        @Expose
        @SerializedName("long")
        var long: String,

        @Expose
        @SerializedName("created_at")
        var created_at: String,

        @Expose
        @SerializedName("updated_at")
        var updated_at: String,

        @Expose
        @SerializedName("place_id")
        var place_id: String,

        @Expose
        @SerializedName("alamat")
        var alamat: String,

        @Expose
        @SerializedName("jarak")
        var jarak: Double,

        @Expose
        @SerializedName("visible")
        var visible: Boolean
): Serializable