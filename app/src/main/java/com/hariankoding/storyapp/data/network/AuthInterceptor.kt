package com.hariankoding.storyapp.data.network

import android.content.Context
import com.hariankoding.storyapp.utils.SharedPreferencesHelper
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(context: Context) : Interceptor {
    private val sessionManager = SharedPreferencesHelper.invoke(context)
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        sessionManager.fetchAuthToken()?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }
        return chain.proceed(requestBuilder.build())
    }
}