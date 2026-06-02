package com.dmg.muslimapp.ui.masjid.favorit

import android.util.Log
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.dmg.muslimapp.data.network.ApiEndPoint
import com.dmg.muslimapp.data.network.BasicAuthInterceptor
import com.dmg.muslimapp.data.network.model.masjid.MasjidList
import com.google.gson.Gson
import okhttp3.OkHttpClient
import org.json.JSONArray


class MasjidListFavoritPresenter(private var mView: MasjidListFavoritView?): MasjidListFavoritPresenterView {
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
                                "        \"nama_tempat\": \"Masjid Al - Hikmah\",\n" +
                                "        \"jenis\": \"Masjid\",\n" +
                                "        \"lat\": \"-6.92562200\",\n" +
                                "        \"long\": \"107.60933200\",\n" +
                                "        \"created_at\": \"2018-03-09 05:29:40\",\n" +
                                "        \"updated_at\": \"2018-03-09 05:29:40\",\n" +
                                "        \"place_id\": \"ChIJfZFXKybmaC4RAL6w7CvNSu8\",\n" +
                                "        \"alamat\": \"Gang Cakradireja, Balonggede\",\n" +
                                "        \"jarak\": 0.36492178562131\n" +
                                "    },\n" +
                                "    {\n" +
                                "        \"id\": \"2\",\n" +
                                "        \"nama_tempat\": \"Masjid Al- Ikhlas\",\n" +
                                "        \"jenis\": \"Masjid\",\n" +
                                "        \"lat\": \"-6.92523700\",\n" +
                                "        \"long\": \"107.60921600\",\n" +
                                "        \"created_at\": \"2018-03-09 05:29:40\",\n" +
                                "        \"updated_at\": \"2018-03-09 05:29:40\",\n" +
                                "        \"place_id\": \"ChIJw2vpLibmaC4RjXTk5DzJ3K8\",\n" +
                                "        \"alamat\": \"Gang Cakradireja, Balonggede\",\n" +
                                "        \"jarak\": 0.38014491579702\n" +
                                "    },\n" +
                                "    {\n" +
                                "        \"id\": \"3\",\n" +
                                "        \"nama_tempat\": \"Masjid As Salam\",\n" +
                                "        \"jenis\": \"Masjid\",\n" +
                                "        \"lat\": \"-6.92570300\",\n" +
                                "        \"long\": \"107.60960300\",\n" +
                                "        \"created_at\": \"2018-03-09 05:29:40\",\n" +
                                "        \"updated_at\": \"2018-03-09 05:29:40\",\n" +
                                "        \"place_id\": \"ChIJc2FH1SfmaC4R9gMa-lP1Fhk\",\n" +
                                "        \"alamat\": \"JL. Sasakgantung, RT. 05/04, Balonggede\",\n" +
                                "        \"jarak\": 0.38572172741667\n" +
                                "    },\n" +
                                "    {\n" +
                                "        \"id\": \"4\",\n" +
                                "        \"nama_tempat\": \"Masjid Al Falah\",\n" +
                                "        \"jenis\": \"Masjid\",\n" +
                                "        \"lat\": \"-6.92604820\",\n" +
                                "        \"long\": \"107.60983070\",\n" +
                                "        \"created_at\": \"2018-03-09 05:29:40\",\n" +
                                "        \"updated_at\": \"2018-03-09 05:29:40\",\n" +
                                "        \"place_id\": \"ChIJbXLoeCjmaC4RiMoc_M20Z9o\",\n" +
                                "        \"alamat\": \"Jalan Sasak Gantung No.17, Balonggede\",\n" +
                                "        \"jarak\": 0.39119094942715\n" +
                                "    },\n" +
                                "    {\n" +
                                "        \"id\": \"5\",\n" +
                                "        \"nama_tempat\": \"Masjid SMA Pasundan1\",\n" +
                                "        \"jenis\": \"Masjid\",\n" +
                                "        \"lat\": \"-6.92369760\",\n" +
                                "        \"long\": \"107.60762250\",\n" +
                                "        \"created_at\": \"2018-03-09 05:29:40\",\n" +
                                "        \"updated_at\": \"2018-03-09 05:29:40\",\n" +
                                "        \"place_id\": \"ChIJCehIcCbmaC4ReYyqWpfkdGA\",\n" +
                                "        \"alamat\": \"Jalan Balongan Gede No.28, Balonggede\",\n" +
                                "        \"jarak\": 0.43036577350207\n" +
                                "    },\n" +
                                "    {\n" +
                                "        \"id\": \"6\",\n" +
                                "        \"nama_tempat\": \"Masjid Miftahusalam\",\n" +
                                "        \"jenis\": \"Masjid\",\n" +
                                "        \"lat\": \"-6.92542300\",\n" +
                                "        \"long\": \"107.61020500\",\n" +
                                "        \"created_at\": \"2018-03-09 05:29:39\",\n" +
                                "        \"updated_at\": \"2018-03-09 05:29:39\",\n" +
                                "        \"place_id\": \"ChIJbwTwhijmaC4RqzY2zcDNy1k\",\n" +
                                "        \"alamat\": \"Gang Cakradireja, Balonggede\",\n" +
                                "        \"jarak\": 0.45890282779425\n" +
                                "    },\n" +
                                "    {\n" +
                                "        \"id\": \"7\",\n" +
                                "        \"nama_tempat\": \"Masjid Darul Hikmah\",\n" +
                                "        \"jenis\": \"Masjid\",\n" +
                                "        \"lat\": \"-6.92876600\",\n" +
                                "        \"long\": \"107.61054400\",\n" +
                                "        \"created_at\": \"2018-03-09 05:29:39\",\n" +
                                "        \"updated_at\": \"2018-03-09 05:29:39\",\n" +
                                "        \"place_id\": \"ChIJGcA-pYLoaC4RC9962TUSn5w\",\n" +
                                "        \"alamat\": \"Jalan A. Warsoma No. 5, Balonggede, Regol, Balonggede\",\n" +
                                "        \"jarak\": 0.46345331752393\n" +
                                "    },\n" +
                                "    {\n" +
                                "        \"id\": \"8\",\n" +
                                "        \"nama_tempat\": \"Masjid Darul Hikmah\",\n" +
                                "        \"jenis\": \"Masjid\",\n" +
                                "        \"lat\": \"-6.92876600\",\n" +
                                "        \"long\": \"107.61054400\",\n" +
                                "        \"created_at\": \"2018-03-09 05:29:39\",\n" +
                                "        \"updated_at\": \"2018-03-09 05:29:39\",\n" +
                                "        \"place_id\": \"ChIJGcA-pYLoaC4RC9962TUSn5w\",\n" +
                                "        \"alamat\": \"Jalan A. Warsoma No. 5, Balonggede, Regol, Balonggede\",\n" +
                                "        \"jarak\": 0.46345331752393\n" +
                                "    },\n" +
                                "    {\n" +
                                "        \"id\": \"9\",\n" +
                                "        \"nama_tempat\": \"Masjid Darul Hikmah\",\n" +
                                "        \"jenis\": \"Masjid\",\n" +
                                "        \"lat\": \"-6.92876600\",\n" +
                                "        \"long\": \"107.61054400\",\n" +
                                "        \"created_at\": \"2018-03-09 05:29:39\",\n" +
                                "        \"updated_at\": \"2018-03-09 05:29:39\",\n" +
                                "        \"place_id\": \"ChIJGcA-pYLoaC4RC9962TUSn5w\",\n" +
                                "        \"alamat\": \"Jalan A. Warsoma No. 5, Balonggede, Regol, Balonggede\",\n" +
                                "        \"jarak\": 0.46345331752393\n" +
                                "    },\n" +
                                "    {\n" +
                                "        \"id\": \"10\",\n" +
                                "        \"nama_tempat\": \"Masjid Darul Hikmah\",\n" +
                                "        \"jenis\": \"Masjid\",\n" +
                                "        \"lat\": \"-6.92876600\",\n" +
                                "        \"long\": \"107.61054400\",\n" +
                                "        \"created_at\": \"2018-03-09 05:29:39\",\n" +
                                "        \"updated_at\": \"2018-03-09 05:29:39\",\n" +
                                "        \"place_id\": \"ChIJGcA-pYLoaC4RC9962TUSn5w\",\n" +
                                "        \"alamat\": \"Jalan A. Warsoma No. 5, Balonggede, Regol, Balonggede\",\n" +
                                "        \"jarak\": 0.46345331752393\n" +
                                "    }" +
                                "]"

                        val list: MutableList<MasjidList> = Gson().fromJson(res.toString() , Array<MasjidList>::class.java).toMutableList()

                        if(list == null) {
                            //mView.getFailed(responses.getMessage())
                            Log.e("Response", "Data not found")
                        }else{
                            mView?.showMasjid(list)

                            Log.e("Response", "Masjid List OK")
                        }
                    }

                    override fun onError(error: ANError) {
                        // handle error
                        error.printStackTrace()
                        Log.e("Response", error.toString())
                    }
                })
    }

    // Destroy View when Activity destroyed
    override fun onDestroy() {
        mView = null
    }
}