package com.hariankoding.storyapp.remote

import com.hariankoding.storyapp.model.LoginResponse
import com.hariankoding.storyapp.model.Response
import com.hariankoding.storyapp.model.StoriesResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Response

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @Multipart
    @POST("stories")
    suspend fun uploadStories(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    ): Response

    @GET("stories")
    suspend fun allStories(
    ): StoriesResponse
}