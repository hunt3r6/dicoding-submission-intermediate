package com.hariankoding.storyapp.utils

import com.hariankoding.storyapp.data.network.ApiService
import com.hariankoding.storyapp.data.network.model.ListStoryItem
import com.hariankoding.storyapp.data.network.model.LoginResponse
import com.hariankoding.storyapp.data.network.model.Response
import com.hariankoding.storyapp.data.network.model.StoriesResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

class FakeApiService : ApiService {

    override suspend fun register(name: String, email: String, password: String): Response {
        TODO("Not yet implemented")
    }

    override suspend fun login(email: String, password: String): LoginResponse {
        TODO("Not yet implemented")
    }

    override suspend fun uploadStories(
        file: MultipartBody.Part,
        description: RequestBody
    ): Response {
        TODO("Not yet implemented")
    }

    override suspend fun allStories(page: Int, size: Int, location: Int): StoriesResponse {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                id = i.toString(),
                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1666845359145_osWD6l_I.jpg",
                createdAt = "2022-10-27T04:35:59.148Z",
                name = "nama $i",
                description = "desc $i",
                lon = 0.0,
                lat = 0.0
            )
            items.add(story)
        }
        items.subList((page - 1) * size, (page - 1) * size + size)
        return StoriesResponse(
            error = false,
            message = "Stories fetched successfully",
            listStory = items
        )
    }
}