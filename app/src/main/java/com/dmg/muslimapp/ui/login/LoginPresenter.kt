package com.dmg.muslimapp.ui.login

import android.content.Context
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import android.util.Log
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.dmg.muslimapp.data.db.AppDbHandler
import com.dmg.muslimapp.data.db.model.users.UserProfile
import com.dmg.muslimapp.data.network.ApiEndPoint
import com.dmg.muslimapp.data.network.BasicAuthInterceptor
import com.dmg.muslimapp.data.network.model.Login
import com.dmg.muslimapp.data.network.model.Profile
import com.dmg.muslimapp.data.prefs.AppPreference
import com.dmg.muslimapp.utils.LoggedStatus
import com.google.gson.Gson
import okhttp3.*
import org.json.JSONObject


class LoginPresenter(private var mView: LoginView?) : LoginPresenterView {

    private lateinit var context: Context

    override fun getLogin(context: Context, username: String, pass: String) {

        this.context = context

        Log.e("Login", "$username, $pass")

        AndroidNetworking.post(ApiEndPoint.ENDPOINT_LOGIN)
                .addBodyParameter("email", username)
                .addBodyParameter("password", pass)
                .setOkHttpClient(OkHttpClient.Builder()
                        .addInterceptor(BasicAuthInterceptor())
                        .build())
                .setTag("UserLogin")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener{
                    override fun onResponse(response: JSONObject?) {
                        val responses = Gson().fromJson(response.toString(), Login.Response::class.java)

                        if (responses.status == "success") {
                            //mView?.loginSuccess(responses.data)
                            val user_id= responses.data.id
                            getProfile(user_id)
                        } else {
                            mView?.loginFailed(responses.message)
                        }
                    }

                    override fun onError(error: ANError) {
                        // handle error
                        error.printStackTrace()
                        Log.e("Response Login", error.errorBody)
                        mView?.loginFailed("Login Failed")
                    }
                })

    }

    override fun getProfile(id: String){
        Log.e("Login", "$id")

        AndroidNetworking.get(ApiEndPoint.ENDPOINT_PROFILE)
                .addQueryParameter("id", id)
                .setOkHttpClient(OkHttpClient.Builder()
                        .addInterceptor(BasicAuthInterceptor())
                        .build())
                .setTag("UserProfile")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener{
                    override fun onResponse(response: JSONObject?) {
                        val responses = Gson().fromJson(response.toString(), Profile::class.java)

                        if (responses.data != null) {
                            setProfile(responses.data)
                        } else {
                            Log.e("Response Profile", "Failed")
                            mView?.loginFailed("Login Failed")
                        }
                    }

                    override fun onError(error: ANError) {
                        // handle error
                        error.printStackTrace()
                        Log.e("Response Profile", error.errorBody)
                        mView?.loginFailed("Login Failed")
                    }
                })
    }

    fun setProfile(userProfile: UserProfile){
        val dbHandler = AppDbHandler(context)
        val insert = dbHandler.addOrUpdateUserProfile(userProfile)
        if(insert){
            val db2 = AppDbHandler(context)
            val userProf = db2.getUserProfile()

            if(userProf?.userId.toString() != ""){
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
        }
    }

    // Destroy View when Activity destroyed
    override fun onDestroy() {
        mView = null
    }
}
