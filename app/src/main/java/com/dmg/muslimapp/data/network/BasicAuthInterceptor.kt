package com.dmg.muslimapp.data.network

import com.dmg.muslimapp.utils.Constants

import java.io.IOException

import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response

class BasicAuthInterceptor : Interceptor {
    private val credentials: String

    init {
        this.credentials = Credentials.basic(Constants.BASIC_AUTH_NEW_USERNAME, Constants.BASIC_AUTH_NEW_PASSWORD)
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val authenticatedRequest = request.newBuilder()
                .header("Authorization", credentials).build()
        return chain.proceed(authenticatedRequest)
    }
}
