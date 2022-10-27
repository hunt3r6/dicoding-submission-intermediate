package com.hariankoding.storyapp.data

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.hariankoding.storyapp.data.database.StoryDatabase
import com.hariankoding.storyapp.data.database.StoryRemoteMediator
import com.hariankoding.storyapp.data.database.entity.ListStoryEntity
import com.hariankoding.storyapp.data.network.ApiService
import com.hariankoding.storyapp.data.network.model.LoginResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

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

    suspend fun login(email: String, password: String): LoginResponse =
        apiService.login(email, password)

    suspend fun register(name: String, email: String, password: String) =
        apiService.register(name, email, password)


    suspend fun uploadStories(file: MultipartBody.Part, description: RequestBody) =
        apiService.uploadStories(file, description)

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

}