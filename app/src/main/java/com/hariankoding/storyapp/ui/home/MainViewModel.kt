package com.hariankoding.storyapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.hariankoding.storyapp.data.Repository
import com.hariankoding.storyapp.data.database.entity.ListStoryEntity
import com.hariankoding.storyapp.data.network.model.StoriesResponse
import com.hariankoding.storyapp.utils.Result

class MainViewModel(private val repository: Repository) : ViewModel() {
    private var _listStoryResponse = MutableLiveData<Result<StoriesResponse>>()
    val listStoryResponse: LiveData<PagingData<ListStoryEntity>> =
        repository.allStories().cachedIn(viewModelScope)

    /*fun loadStory() {
        _listStoryResponse.postValue(Result.Loading)
        viewModelScope.launch {
            try {
                val data = repository.allStories()
                _listStoryResponse.postValue(Result.Success(data))
            } catch (e: Exception) {
                _listStoryResponse.postValue(Result.Error(e.toString()))
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
    }*/

}