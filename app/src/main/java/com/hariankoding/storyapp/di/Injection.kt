package com.hariankoding.storyapp.di

import android.content.Context
import com.hariankoding.storyapp.remote.ApiConfig
import com.hariankoding.storyapp.remote.Repository

object Injection {

    fun provideRepository(context: Context): Repository {
        val apiService = ApiConfig().getApiService(context)
        return Repository.getInstance(apiService)
    }
}