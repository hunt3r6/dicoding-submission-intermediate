package com.hariankoding.storyapp.remote

import com.hariankoding.storyapp.model.LoginResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

class Repository private constructor(
    private val apiService: ApiService
) {
    companion object {
        private var instance: Repository? = null
        fun getInstance(
            apiService: ApiService,
        ): Repository = instance ?: synchronized(this) {
            instance ?: Repository(apiService)
        }
    }

    suspend fun login(email: String, password: String): LoginResponse =
        apiService.login(email, password)

    suspend fun register(name: String, email: String, password: String) =
        apiService.register(name, email, password)


    suspend fun uploadStories(file: MultipartBody.Part, description: RequestBody) =
        apiService.uploadStories(file, description)

    suspend fun allStories() = apiService.allStories()

}