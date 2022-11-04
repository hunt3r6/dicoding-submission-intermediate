package com.hariankoding.storyapp.ui.createstory

import androidx.lifecycle.ViewModel
import com.hariankoding.storyapp.data.Repository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class CreateStoryViewModel(private val repository: Repository) : ViewModel() {

    fun uploadImage(file: MultipartBody.Part, description: RequestBody) =
        repository.uploadStories(file, description)
}