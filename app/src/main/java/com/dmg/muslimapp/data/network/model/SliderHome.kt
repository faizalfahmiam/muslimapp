package com.dmg.muslimapp.data.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SliderHome {
        class SliderResponse(ref: String, data: List<SliderData>) {
                @Expose
                @SerializedName("ref")
                private var ref: String? = null

                @Expose
                @SerializedName("data")
                private var data: List<SliderData>?

                init {
                        this.ref = ref
                        this.data = data
                }

                fun getRef(): String? {
                        return ref
                }

                fun setRef(ref: String) {
                        this.ref = ref
                }

                fun getData(): List<SliderData>? {
                        return data
                }

                fun setData(data: List<SliderData>) {
                        this.data = data
                }
        }

        class SliderData(url: String, gambar: String) {
                @Expose
                @SerializedName("link_url_android")
                private var url: String? = null

                @Expose
                @SerializedName("gambar")
                private var gambar: String? = null

                init {
                        this.url = url
                        this.gambar = gambar
                }

                fun getUrl(): String? {
                        return url
                }

                fun setUrl(url: String) {
                        this.url = url
                }

                fun getGambar(): String? {
                        return gambar
                }

                fun setGambar(gambar: String) {
                        this.gambar = gambar
                }
        }
}



