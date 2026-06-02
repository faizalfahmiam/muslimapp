package com.dmg.muslimapp.ui.main

import android.content.Context
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import android.util.Log
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.dmg.muslimapp.data.db.AppDbHandler
import com.dmg.muslimapp.data.db.AppDbQuranHandler
import com.dmg.muslimapp.data.db.model.quran.Ayah
import com.dmg.muslimapp.data.db.model.service_menu.ServiceMenu
import com.dmg.muslimapp.data.network.ApiEndPoint
import com.dmg.muslimapp.data.network.BasicAuthInterceptor
import com.dmg.muslimapp.data.network.model.SliderHome
import com.google.gson.Gson
import okhttp3.*
import org.json.JSONObject


class MainPresenter(private var mView: MainView?) : MainPresenterView {
    private lateinit var context: Context

    override fun getSliderResources(context: Context, size: Int, locale: String) {
        this.context = context

        Log.e("parameter",size.toString() + " " +locale)

        AndroidNetworking.get(ApiEndPoint.ENDPOINT_HOME_SLIDER)
                .addQueryParameter("size", size.toString())
                .addQueryParameter("bahasa", locale)
                .setOkHttpClient(OkHttpClient.Builder()
                        .addInterceptor(BasicAuthInterceptor())
                        .build())
                .setTag("HomeSlider")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject) {
                        // do anything with response

                        var responses = Gson().fromJson(response.toString(), SliderHome.SliderResponse::class.java)

                        if (responses.getRef() == null) {
                            //mView.loginFailed(responses.getMessage())
                            Log.e("Response", "Data not found")
                        } else {
                            mView?.updateSliderResource(responses.getData()!!)
                            /*for(item in responses.getData()!!){
                                Log.e("Item", item.getGambar())
                            }*/
                        }
                    }

                    override fun onError(error: ANError) {
                        // handle error
                        error.printStackTrace()
                        Log.e("Response", error.toString())
                    }
                })

    }

    override fun getServiceMenuList(context: Context){
        val dbHandler = AppDbHandler(context)
        var listMenu: List<ServiceMenu> = ArrayList<ServiceMenu>()
        listMenu = dbHandler.getServiceMenuList()
        mView?.showMenu(listMenu)
    }

    override fun getFeedVerse(context: Context){
        val dbHandler = AppDbQuranHandler(context)
        var list: List<Ayah> = ArrayList<Ayah>()
        list = dbHandler.getFeedAyah()
        mView?.showFeedVerse(list)
    }

    // Destroy View when Activity destroyed
    override fun onDestroy() {
        mView = null
    }
}
