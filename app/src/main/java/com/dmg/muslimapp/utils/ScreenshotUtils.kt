package com.dmg.muslimapp.utils

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import android.view.View
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import android.content.Intent
import android.net.Uri


class ScreenshotUtils {
    companion object {
        /*  Method which will return Bitmap after taking screenshot. We have to pass the view which we want to take screenshot.  */
        fun getScreenShot(view: View): Bitmap {
            val screenView = view.getRootView()
            screenView.setDrawingCacheEnabled(true)
            val bitmap = Bitmap.createBitmap(screenView.getDrawingCache())
            screenView.setDrawingCacheEnabled(false)
            return bitmap
        }

        /*  Create Directory where screenshot will save for sharing screenshot  */
        fun getMainDirectoryName(context: Context): File {
            //Here we will use getExternalFilesDir and inside that we will make our Demo folder
            //benefit of getExternalFilesDir is that whenever the app uninstalls the images will get deleted automatically.
            val mainDir = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "Share")

            //If File is not present create directory
            if (!mainDir.exists()) {
                if (mainDir.mkdir())
                    Log.e("Create Directory", "Main Directory Created : $mainDir")
            }
            return mainDir
        }

        /*  Store taken screenshot into above created path  */
        fun store(bm: Bitmap, fileName: String, saveFilePath: File): File {
            val dir = File(saveFilePath.getAbsolutePath())
            if (!dir.exists())
                dir.mkdirs()
            val file = File(saveFilePath.getAbsolutePath(), fileName)
            try {
                val fOut = FileOutputStream(file)
                bm.compress(Bitmap.CompressFormat.JPEG, 85, fOut)
                fOut.flush()
                fOut.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return file
        }

        /*  Share Screenshot  */
        fun shareScreenshot(context: Context, file: File, text: String) {
            val uri = Uri.fromFile(file)//Convert file path into Uri for sharing
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.type = "image/*"
            intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Text")
            intent.putExtra(android.content.Intent.EXTRA_TEXT, text)
            intent.putExtra(Intent.EXTRA_STREAM, uri)//pass uri here
            context.startActivity(Intent.createChooser(intent, "Share Masjid"))
        }
    }
}