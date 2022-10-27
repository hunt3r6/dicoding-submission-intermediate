package com.hariankoding.storyapp.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hariankoding.storyapp.data.network.model.Response
import com.hariankoding.storyapp.data.Repository
import com.hariankoding.storyapp.utils.Result
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class RegisterViewModel(private val repository: Repository) : ViewModel() {
    private var _registerResponse = MutableLiveData<Result<Response>>()
    val registerResponse: LiveData<Result<Response>> get() = _registerResponse

    fun register(name: String, email: String, password: String) {
        _registerResponse.postValue(Result.Loading)
        viewModelScope.launch {
            try {
                val data = repository.register(name, email, password)
                _registerResponse.postValue(Result.Success(data))
            } catch (e: Exception) {
                when (e) {
                    is IOException -> {
                        _registerResponse.postValue(
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
                        _registerResponse.postValue(
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