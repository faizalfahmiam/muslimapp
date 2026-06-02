package com.dmg.muslimapp.data.db.model.users

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UserProfile(
        @Expose
        @SerializedName("index")
        var index: Int = 0,

        @Expose
        @SerializedName("id")
        var userId: Int = 0,

        @Expose
        @SerializedName("nama")
        var nama: String,

        @Expose
        @SerializedName("no_hp")
        var noHp: String,

        @Expose
        @SerializedName("email")
        var email: String,

        @Expose
        @SerializedName("created_at")
        var createdAt: String,

        @Expose
        @SerializedName("updated_at")
        var updateAt: String,

        @Expose
        @SerializedName("profile_picture")
        var profilePicture: String,

        @Expose
        @SerializedName("tgl_lahir")
        var tglLahir: String,

        @Expose
        @SerializedName("alamat")
        var alamat: String
)

/*class UserProfile {
    @Expose
    @SerializedName("index")
    private var index: Int = 0

    @Expose
    @SerializedName("id")
    private var userId: Int = 0

    @Expose
    @SerializedName("nama")
    private var nama: String

    @Expose
    @SerializedName("no_hp")
    private var noHp: String

    @Expose
    @SerializedName("email")
    private var email: String

    @Expose
    @SerializedName("created_at")
    private var createdAt: String

    @Expose
    @SerializedName("updated_at")
    private var updateAt: String

    @Expose
    @SerializedName("profile_picture")
    private var profilePicture: String

    @Expose
    @SerializedName("tgl_lahir")
    private var tglLahir: String

    @Expose
    @SerializedName("alamat")
    private var alamat: String

    constructor(index: Int, id: Int, nama: String, noHp: String, email: String, createdAt: String, updateAt: String, profilePicture: String, tglLahir: String, alamat: String) {
        this.index = index
        this.userId = id
        this.nama = nama
        this.noHp = noHp
        this.email = email
        this.createdAt = createdAt
        this.updateAt = updateAt
        this.profilePicture = profilePicture
        this.tglLahir = tglLahir
        this.alamat = alamat
    }

    fun getIndex(): Int {
        return index
    }

    fun setIndex(index: Int) {
        this.index = index
    }

    fun getId(): Int {
        return userId
    }

    fun setId(userId: Int) {
        this.userId = userId
    }

    fun getNama(): String {
        return nama
    }

    fun setNama(nama: String) {
        this.nama = nama
    }

    fun getNoHp(): String {
        return noHp
    }

    fun setNoHp(noHp: String) {
        this.noHp = noHp
    }

    fun getEmail(): String {
        return email
    }

    fun setEmail(email: String) {
        this.email = email
    }

    fun getCreatedAt(): String {
        return createdAt
    }

    fun setCreatedAt(createdAt: String) {
        this.createdAt = createdAt
    }

    fun getUpdateAt(): String {
        return updateAt
    }

    fun setUpdateAt(updateAt: String) {
        this.updateAt = updateAt
    }

    fun getProfilePicture(): String {
        return profilePicture
    }

    fun setProfilePicture(profilePicture: String) {
        this.profilePicture = profilePicture
    }

    fun getIdUser(): Int {
        return userId
    }

    fun setIdUser(idUser: Int) {
        this.userId = userId
    }

    fun getTglLahir(): String {
        return tglLahir
    }

    fun setTglLahir(tglLahir: String) {
        this.tglLahir = tglLahir
    }

    fun getAlamat(): String {
        return alamat
    }

    fun setAlamat(alamat: String) {
        this.alamat = alamat
    }
}*/