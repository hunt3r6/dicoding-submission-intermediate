package com.hariankoding.storyapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hariankoding.storyapp.data.database.StoryDatabase
import com.hariankoding.storyapp.data.database.StoryRemoteMediator
import com.hariankoding.storyapp.data.database.entity.ListStoryEntity
import com.hariankoding.storyapp.data.network.ApiService
import com.hariankoding.storyapp.data.network.model.ListStoryItem
import com.hariankoding.storyapp.data.network.model.LoginResponse
import com.hariankoding.storyapp.data.network.model.Response
import com.hariankoding.storyapp.utils.Result
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import java.io.IOException

class Repository private constructor(
    private val apiService: ApiService,
    private val database: StoryDatabase
) {
    companion object {
        private var instance: Repository? = null
        fun getInstance(
            apiService: ApiService,
            database: StoryDatabase
        ): Repository = instance ?: synchronized(this) {
            instance ?: Repository(apiService, database)
        }
    }

    fun login(email: String, password: String): LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val data = apiService.login(email, password)
            emit(Result.Success(data))
        } catch (e: Exception) {
            when (e) {
                is IOException -> {
                    emit(
                        Result.Error(
                            "Cek Koneksi Internet"
                        )
                    )
                }
                is HttpException -> {
                    val gson = Gson()
                    val type = object : TypeToken<Response>() {}.type
                    val errorResponse: Response? =
                        gson.fromJson(e.response()?.errorBody()!!.charStream(), type)
                    emit(
                        Result.Error(
                            errorResponse?.message ?: ""
                        )

                    )
                }
            }
        }

    }

    fun register(
        name: String,
        email: String,
        password: String
    ): LiveData<Result<Response>> = liveData {
        emit(Result.Loading)
        try {
            val data = apiService.register(name, email, password)
            emit(Result.Success(data))
        } catch (e: Exception) {
            when (e) {
                is IOException -> {
                    emit(
                        Result.Error(
                            "Cek Koneksi Internet"
                        )
                    )
                }
                is HttpException -> {
                    val gson = Gson()
                    val type = object : TypeToken<Response>() {}.type
                    val errorResponse: Response? =
                        gson.fromJson(e.response()?.errorBody()!!.charStream(), type)
                    emit(
                        Result.Error(
                            errorResponse?.message ?: ""
                        )
                    )
                }
            }
        }

    }


    fun uploadStories(
        file: MultipartBody.Part,
        description: RequestBody
    ): LiveData<Result<Response>> = liveData {
        emit(Result.Loading)
        try {
            val data = apiService.uploadStories(file, description)
            emit(Result.Success(data))
        } catch (e: Exception) {
            emit(Result.Error(e.toString()))
        }
    }


    fun allStories(): LiveData<PagingData<ListStoryEntity>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(database, apiService),
            pagingSourceFactory = {
                database.storyDao().getAllStory()
            }
        ).liveData
    }

    fun locationStory(): LiveData<Result<List<ListStoryItem>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.allStories(location = 1)
            val listStory = response.listStory
            emit(Result.Success(listStory))
        } catch (e: Exception) {
            emit(Result.Error(e.toString()))
        }

    }


}