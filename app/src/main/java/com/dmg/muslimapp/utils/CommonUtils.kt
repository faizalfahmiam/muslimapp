package com.dmg.muslimapp.utils

import android.app.ProgressDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Address
import android.net.Uri
import android.text.TextUtils
import com.dmg.muslimapp.R
import com.dmg.muslimapp.data.db.model.users.UserLocation
import java.text.DecimalFormat

object CommonUtils {
    fun getURLForResource(context: Context, resourceId: Int): String {
        return Uri.parse("android.resource://" + context.packageName + "/" + resourceId).toString()
    }

    fun getShortAddress(result: Address?): String {
        var address = ""
        if (result != null) {
            if (!TextUtils.isEmpty(result.subAdminArea)) {
                address += result.subAdminArea + ", "
            } else if (!TextUtils.isEmpty(result.locality)) {
                address += result.locality + ", "
            } else if (!TextUtils.isEmpty(result.adminArea)) {
                address += result.adminArea + ", "
            }
            if (!TextUtils.isEmpty(result.countryName)) {
                address += result.countryName + ", "
            }
            if (address.contains(", ")) {
                address = address.substring(0, address.length - 2)
            }
        }
        return address
    }

    fun getShortAddressCity(userLocation: UserLocation?): String {
        var address = ""
        if (userLocation != null) {
            if (!TextUtils.isEmpty(userLocation.city)) {
                address += userLocation.city
            } else if (!TextUtils.isEmpty(userLocation.subState)) {
                address += userLocation.subState
            } else if (!TextUtils.isEmpty(userLocation.state)) {
                address += userLocation.state
            }
        }
        return address
    }

    fun decimal2angka(angka: Double): String {
        val a1 = angka

        val twoDForm = DecimalFormat("#.##")
        val a2 = twoDForm.format(a1)
        return a2
    }

    fun resizeMapIcons(context: Context, iconName: String, width: Int, height: Int): Bitmap {
        val imageBitmap = BitmapFactory.decodeResource(context.resources, context.resources.getIdentifier(iconName, "drawable", context.packageName));
        val resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }

    @Suppress("DEPRECATION")
    fun showLoadingDialog(context: Context): ProgressDialog {
        val progressDialog = ProgressDialog(context)
        progressDialog.show()
        if (progressDialog.window != null) {
            progressDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        progressDialog.setContentView(R.layout.progress_dialog)
        progressDialog.isIndeterminate = true
        progressDialog.setCancelable(false)
        progressDialog.setCanceledOnTouchOutside(false)
        return progressDialog
    }
}
