package com.hariankoding.storyapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hariankoding.storyapp.model.Response
import com.hariankoding.storyapp.model.StoriesResponse
import com.hariankoding.storyapp.remote.Repository
import com.hariankoding.storyapp.utils.Result
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class MainViewModel(private val repository: Repository) : ViewModel() {
    private var _listStoryResponse = MutableLiveData<Result<StoriesResponse>>()
    val listStoryResponse: LiveData<Result<StoriesResponse>> get() = _listStoryResponse

    init {
        loadStory()
    }

    private fun loadStory() {
        _listStoryResponse.postValue(Result.Loading)
        viewModelScope.launch {
            try {
                val data = repository.allStories()
                _listStoryResponse.postValue(Result.Success(data))
            } catch (e: Exception) {
                when (e) {
                    is IOException -> {
                        _listStoryResponse.postValue(
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
                        _listStoryResponse.postValue(
                            Result.Error(
                                errorResponse?.message ?: ""
                            )

                        )
                    }

                }
            }

        }
    }

}