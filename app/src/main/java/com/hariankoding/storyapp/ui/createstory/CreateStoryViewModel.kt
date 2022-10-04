package com.hariankoding.storyapp.ui.createstory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hariankoding.storyapp.model.Response
import com.hariankoding.storyapp.remote.Repository
import com.hariankoding.storyapp.utils.Result
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class CreateStoryViewModel(private val repository: Repository) : ViewModel() {
    private var _uploadResponse = MutableLiveData<Result<Response>>()
    val uploadResponse: LiveData<Result<Response>> get() = _uploadResponse

    fun uploadImage(file: MultipartBody.Part, description: RequestBody){
        _uploadResponse.postValue(Result.Loading)
        viewModelScope.launch {
            try {
                val data = repository.uploadStories(file, description)
                _uploadResponse.postValue(Result.Success(data))
            }catch (e:Exception) {

            }

        }
    }
}