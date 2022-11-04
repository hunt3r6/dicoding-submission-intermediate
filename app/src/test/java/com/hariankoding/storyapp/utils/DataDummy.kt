package com.hariankoding.storyapp.utils

import com.hariankoding.storyapp.data.database.entity.ListStoryEntity
import com.hariankoding.storyapp.data.network.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

object DataDummy {

    fun generateDummyStoryResponse(): List<ListStoryEntity> {
        val item: MutableList<ListStoryEntity> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryEntity(
                id = i.toString(),
                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1666845359145_osWD6l_I.jpg",
                createdAt = "2022-10-27T04:35:59.148Z",
                name = "nama $i",
                description = "desc $i",
                lon = 0.0,
                lat = 0.0
            )
            item.add(story)
        }
        return item
    }

    fun generateDummyLocationResponse(): StoriesResponse {
        val item: MutableList<ListStoryItem> = arrayListOf()
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
            item.add(story)
        }
        return StoriesResponse(
            listStory = item,
            error = false,
            message = "Stories fetched successfully"
        )
    }

    fun generateResponseRegister(): Response {
        return Response(
            error = false, message = "success"
        )
    }

    fun generateDummyMultipartFile(): MultipartBody.Part {
        val dummyText = "text"
        return MultipartBody.Part.create(dummyText.toRequestBody())
    }

    fun generateDummyRequestBody(): RequestBody {
        val dummyText = "text"
        return dummyText.toRequestBody()
    }

    fun generateUploadSuccess(): Response {
        return Response(
            error = false, message = "success"
        )
    }

    fun generateResponseLogin(): LoginResponse {
        val loginResult = LoginResult(
            userId = "user-WTdihB1x0Rl1zDRD",
            name = "faisal",
            token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLVdUZGloQjF4MFJsMXpEUkQiLCJpYXQiOjE2NjczMTQ3Njl9.qNWq-yGI2FVuvi8lixoRMkSe4xXp_7JrAzr1jkSfq9A"
        )

        return LoginResponse(
            loginResult = loginResult,
            error = false,
            message = "success"
        )
    }
}