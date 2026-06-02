package com.dmg.muslimapp.data.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AppVersion {

    class Response{
        @Expose
        @SerializedName("code")
        private var code: Int = 0

        @Expose
        @SerializedName("version")
        private lateinit var version: Version

        fun getCode(): Int {
            return code
        }

        fun setCode(code: Int) {
            this.code = code
        }

        fun getVersion(): Version {
            return version
        }

        fun setVersion(version: Version) {
            this.version = version
        }
    }

    class Version{
        @Expose
        @SerializedName("oldest_version")
        private var oldestVersion: String = ""

        @Expose
        @SerializedName("latest_version")
        private var latestVersion: String = ""

        fun getOldestVersion(): String {
            return oldestVersion
        }

        fun setOldestVersion(oldestVersion: String) {
            this.oldestVersion = oldestVersion
        }

        fun getLatestVersion(): String {
            return latestVersion
        }

        fun setLatestVersion(latestVersion: String) {
            this.latestVersion = latestVersion
        }
    }
}