package com.dmg.muslimapp.ui.splash

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.dmg.muslimapp.R
import com.dmg.muslimapp.utils.ToastMessage
import com.afollestad.materialdialogs.MaterialDialog
import com.dmg.muslimapp.data.db.model.service_menu.ServiceMenu
import com.dmg.muslimapp.data.prefs.AppPreference
import com.dmg.muslimapp.ui.main.MainActivity
import com.dmg.muslimapp.ui.setup.SetupActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class SplashActivity : AppCompatActivity(), SplashView {

    private lateinit var mPresenter: SplashPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)

        setup()
    }

    fun setup(){
        mPresenter = SplashPresenter(this)
        //mPresenter.appConfig(this)
        //mPresenter.getVersion(this)
    }

    override fun onResume() {
        super.onResume()
        mPresenter.getVersion()
        configServiceMenu()
    }

    private fun jsonFromAssets(): String? {
        var json: String? = null
        try {
            val `in` = assets.open("json/service_menu.json")
            val size = `in`.available()
            val buffer = ByteArray(size)
            `in`.read(buffer)
            `in`.close()
            json = String(buffer, charset("UTF-8"))
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return json
    }

    fun configServiceMenu(){
        val json = jsonFromAssets()

        try {
            val jsonObject = JSONObject(json)
            val version = jsonObject.getInt("version")
            Log.e("Menu version : ", version.toString())

            val menus = jsonObject.getJSONArray("menus")

            if (menus.length() > 0) {
                //List<ServiceMenu> menu = new ArrayList<>();

                val gson = Gson()
                val menu = gson.fromJson<List<ServiceMenu>>(menus.toString(), object : TypeToken<List<ServiceMenu>>() {

                }.type)

                //showMessage("menus: " + menu.size)
                Log.e("version: ", version.toString())
                Log.e("versionOld: ", AppPreference.getMenuVersion().toString())
                mPresenter.setServiceMenu(menu, version, AppPreference.getMenuVersion(), this)
            } else {
                Log.e("Menu array","not found")
            }
        } catch (e: JSONException) {
            e.printStackTrace()
            Log.e("Error Menu : ", e.message)
        }
    }

    override fun setData(nama: String) {
        // Show data on UI
        ToastMessage("Hallo $nama", this);
    }

    override fun setDataError(strError: String) {
        // Show error on UI
        ToastMessage("Error : $strError", this);
    }

    override fun showForceUpdate(latestVersion: String) {
        MaterialDialog.Builder(this)
                .title(R.string.new_version_is_available)
                .content(R.string.please_update_your_app_on_google_store)
                .positiveText(R.string.action_update)
                .onPositive { dialog, which ->
                    val appPackageName = packageName // getPackageName() from Context or Activity object
                    try {
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName")))
                    } catch (anfe: android.content.ActivityNotFoundException) {
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")))
                    }

                    dialog.dismiss()
                }
                .cancelable(false)
                .build()
                .show()
    }

    override fun showRecommendUpdate(latestVersion: String) {
        MaterialDialog.Builder(this)
                .title(R.string.new_version_is_available)
                .content(R.string.please_update_your_app_on_google_store)
                .positiveText(R.string.action_update)
                .onPositive { dialog, which ->
                    val appPackageName = packageName // getPackageName() from Context or Activity object
                    try {
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName")))
                    } catch (anfe: android.content.ActivityNotFoundException) {
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")))
                    }

                    dialog.dismiss()
                }
                .negativeText(R.string.action_update_later)
                .onNegative(MaterialDialog.SingleButtonCallback { dialog, which -> dialog.dismiss() })
                .cancelable(true)
                .cancelListener(DialogInterface.OnCancelListener { mPresenter.decideNextActivity() })
                .build()
                .show()
    }

    override fun openMainActivity() {
        val intent = MainActivity.getStartIntent(this)
        startActivity(intent)
        finish()
    }

    override fun openSetupActivity(){
        val intent = SetupActivity.getStartIntent(this)
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        // Destroy View
        mPresenter.onDestroy()
        super.onDestroy()
    }
}
