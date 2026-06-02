package com.dmg.muslimapp.data.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Login{
    class Response(
        @Expose
        @SerializedName("ref")
        var ref: String,

        @Expose
        @SerializedName("status")
        var status: String,

        @Expose
        @SerializedName("message")
        var message: String,

        @Expose
        @SerializedName("data")
        var data: Data

    )

    class Data(
        @Expose
        @SerializedName("id")
        var id: String,

        @Expose
        @SerializedName("email")
        var email: String,

        @Expose
        @SerializedName("nama")
        var nama: String,

        @Expose
        @SerializedName("no_hp")
        var no_hp: String,

        @Expose
        @SerializedName("alamat")
        var alamat: String,

        @Expose
        @SerializedName("profile_picture")
        var profile_picture: String,

        @Expose
        @SerializedName("jenis_kelamin")
        var jenis_kelamin: String
    )

}