package com.hariankoding.storyapp.data.network

import com.hariankoding.storyapp.data.network.model.LoginResponse
import com.hariankoding.storyapp.data.network.model.Response
import com.hariankoding.storyapp.data.network.model.StoriesResponse
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
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 5,
        @Query("location") location: Int = 0
    ): StoriesResponse
}