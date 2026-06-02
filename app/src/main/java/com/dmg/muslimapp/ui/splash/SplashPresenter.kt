package com.dmg.muslimapp.ui.splash

import android.content.Context
import android.util.Log
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.dmg.muslimapp.BuildConfig
import com.dmg.muslimapp.data.db.AppDbHandler
import com.dmg.muslimapp.data.db.model.service_menu.ServiceMenu
import com.dmg.muslimapp.data.network.ApiEndPoint
import com.dmg.muslimapp.data.network.model.AppVersion
import com.dmg.muslimapp.data.prefs.AppPreference
import com.dmg.muslimapp.utils.Constants
import com.google.gson.Gson
import okhttp3.Credentials
import okhttp3.OkHttpClient
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class SplashPresenter(private var mView: SplashView?) {

    fun appConfig(context: Context){
        //val mPrefs = AppPreference(context)
        if(AppPreference.isAlreadyConfigured()){
            //mView?.openMainActivity()
            mView?.openSetupActivity()
        }else{
            mView?.openSetupActivity()
        }

    }

    fun getVersion() {

        val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .addInterceptor { chain ->
                    val credential = Credentials.basic(Constants.BASIC_AUTH_NEW_USERNAME, Constants.BASIC_AUTH_NEW_PASSWORD)
                    val request = chain.request().newBuilder()
                            .addHeader("Authorization", credential)
                            .build()

                    chain.proceed(request)
                }
                .build()// socket timeout

        AndroidNetworking.get(ApiEndPoint.ENDPOINT_VERSION)
                .addQueryParameter("device_type", "Android")
                .setOkHttpClient(okHttpClient)
                .setTag("AppCekVersion")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject) {
                        // do anything with response

                        var responses = Gson().fromJson(response.toString(), AppVersion.Response::class.java)

                        if (responses == null) {
                            decideNextActivity()
                            Log.e("Response", "Response not found")
                        } else {
                            Log.e("Response", "OK")

                            if(versionCompare(BuildConfig.VERSION_NAME, responses.getVersion().getOldestVersion()) < 0){
                                mView?.showForceUpdate(responses.getVersion().getLatestVersion())
                                Log.e("Response", "showForceUpdate")
                            }else if(versionCompare(BuildConfig.VERSION_NAME, responses.getVersion().getLatestVersion()) < 0){
                                val shouldShowRecomendation = AppPreference.getLatesVersion() == responses.getVersion().getLatestVersion()
                                if (shouldShowRecomendation) {
                                    AppPreference.setLatesVersion(responses.getVersion().getLatestVersion())
                                    mView?.showRecommendUpdate(responses.getVersion().getLatestVersion())
                                    Log.e("Response", "showRecommendUpdate")
                                } else {
                                    decideNextActivity()
                                }
                            }else{
                                decideNextActivity()
                            }
                        }
                    }

                    override fun onError(error: ANError) {
                        // handle error
                        error.printStackTrace()
                        Log.e("Response", error.toString())

                        decideNextActivity()
                    }
                })
    }

    fun versionCompare(str1: String, str2: String): Int {
        val vals1 = str1.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val vals2 = str2.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        var i = 0
        // set index to first non-equal ordinal or length of shortest version string
        while (i < vals1.size && i < vals2.size && vals1[i] == vals2[i]) {
            i++
        }
        // compare first non-equal ordinal number
        if (i < vals1.size && i < vals2.size) {
            val diff = Integer.valueOf(vals1[i]).compareTo(Integer.valueOf(vals2[i]))
            return Integer.signum(diff)
        }
        // the strings are equal or one string is a substring of the other
        // e.g. "1.2.3" = "1.2.3" or "1.2.3" < "1.2.3.4"
        return Integer.signum(vals1.size - vals2.size)
    }

    fun setServiceMenu(menu: List<ServiceMenu>, menuVersion: Int, versionOld: Int, context: Context){
        if(menu.isNotEmpty()){
            for(serviceMenu in menu){
                Log.e("menu index",serviceMenu.menu_index.toString())
                Log.e("menu id",serviceMenu.menu_id)
                Log.e("menu status",serviceMenu.menu_status.toString())

                val dbHandler = AppDbHandler(context)
                val insert = dbHandler.addOrUpdateServiceMenu(serviceMenu)
                if(insert){
                    Log.e("menu insert",serviceMenu.menu_id.toString())
                }
            }
        }

        /*val dbHandler = AppDbHandler(context)
        val insert = dbHandler.addOrUpdateServiceMenu(menu)
        if(insert){
            val db2 = AppDbHandler(context)
            val serviceMenu = db2.getServiceMenu()

            if(serviceMenu?.userId.toString() != ""){
                AppPreference.setCurrentUserLoggedInMode(LoggedStatus.LoggedInMode.LOGGED_IN_MODE_SERVER)
                mView?.setProfileSuccess()
                Log.e("Set Profile Sukses", userProf?.userId.toString())
            }else{
                mView?.setProfileFailed("Set Profile Failed")
                Log.e("Set Profile", "Get Failed")
            }
        }else{
            mView?.setProfileFailed("Set Profile Failed")
            Log.e("Set Profile", "Insert Failed")
        }*/
    }

    fun decideNextActivity() {
        if(AppPreference.isAlreadyConfigured()){
            mView?.openMainActivity()
            Log.e("TAG","GO-MAIN")
        }else{
            mView?.openSetupActivity()
            Log.e("TAG","GO-SETUP")
        }
    }

    // Destroy View when Activity destroyed
    fun onDestroy() {
        mView = null
    }
}