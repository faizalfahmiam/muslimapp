package com.dmg.muslimapp.ui.masjid.nearby.list

import android.util.Log
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.dmg.muslimapp.data.network.ApiEndPoint
import com.dmg.muslimapp.data.network.BasicAuthInterceptor
import com.dmg.muslimapp.data.network.model.masjid.MasjidNearbyList
import com.google.gson.Gson
import okhttp3.OkHttpClient
import org.json.JSONArray


class MasjidNearbyListPresenter(private var mView: MasjidNearbyListView?): MasjidNearbyListPresenterView {

    override fun getListMasjid(){
        AndroidNetworking.get(ApiEndPoint.ENDPOINT_NEAR)
                .addQueryParameter("lat", "-6.927424")
                .addQueryParameter("long", "107.606569")
                .setOkHttpClient(OkHttpClient.Builder()
                        .addInterceptor(BasicAuthInterceptor())
                        .build())
                .setTag("ListMasjid")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONArray(object : JSONArrayRequestListener{
                    override fun onResponse(response: JSONArray) {

                        val res = "[\n" +
                                "    {\n" +
                                "        \"id\": \"1\",\n" +
                                "        \"nama\": \"Masjid Al - Hikmah\"\n" +
                                "    }," +
                                "    {\n" +
                                "        \"id\": \"2\",\n" +
                                "        \"nama\": \"Masjid Al - Hikmah\"\n" +
                                "    }," +
                                "    {\n" +
                                "        \"id\": \"3\",\n" +
                                "        \"nama\": \"Masjid Al - Hikmah\"\n" +
                                "    }," +
                                "    {\n" +
                                "        \"id\": \"4\",\n" +
                                "        \"nama\": \"Masjid Al - Hikmah\"\n" +
                                "    }," +
                                "    {\n" +
                                "        \"id\": \"5\",\n" +
                                "        \"nama\": \"Masjid Al - Hikmah\"\n" +
                                "    }" +
                                "]"

                        val list: MutableList<MasjidNearbyList> = Gson().fromJson(res.toString() , Array<MasjidNearbyList>::class.java).toMutableList()

                        if(list == null) {
                            //mView.getFailed(responses.getMessage())
                            Log.e("Response", "Data not found")
                        }else{
                            mView?.showPerson(list)

                            Log.e("Response", "Nearby List OK")
                        }
                    }

                    override fun onError(error: ANError) {
                        // handle error
                        error.printStackTrace()
                        Log.e("Response", error.toString())
                    }
                })
    }

    override fun getDetailMasjid(id: String){
        mView?.showMasjidDetail(id)
    }

    // Destroy View when Activity destroyed
    override fun onDestroy() {
        mView = null
    }
}