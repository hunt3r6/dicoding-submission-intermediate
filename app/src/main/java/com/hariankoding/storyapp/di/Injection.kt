package com.hariankoding.storyapp.di

import android.content.Context
import com.hariankoding.storyapp.data.Repository
import com.hariankoding.storyapp.data.database.StoryDatabase
import com.hariankoding.storyapp.data.network.ApiConfig

object Injection {

    fun provideRepository(context: Context): Repository {
        val database = StoryDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService(context)
        return Repository.getInstance(apiService, database)
    }
}