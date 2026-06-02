package com.dmg.muslimapp.di.module

import android.content.Context

import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory
import com.bumptech.glide.module.AppGlideModule

/**
 * For PT. DIGITAL MUSLIM GLOBAL
 * Created by Ridwan Ismail on 06 Februari 2018
 * You can contact me at : ismail.ridwan98@gmail.com
 * -------------------------------------------------
 * MUSLIM APP
 * com.dmg.muslimapp.di.module
 * or see link for more detail https://gitlab.com/iwanz98/muslim-app
 */

@GlideModule
class GlideAppModule : AppGlideModule() {
    override fun applyOptions(context: Context?, builder: GlideBuilder) {
        val diskCacheSizeBytes = 1024 * 1024 * 100 // 100 MB
        builder.setDiskCache(InternalCacheDiskCacheFactory(context, "img_slider", diskCacheSizeBytes.toLong()))
    }
}
