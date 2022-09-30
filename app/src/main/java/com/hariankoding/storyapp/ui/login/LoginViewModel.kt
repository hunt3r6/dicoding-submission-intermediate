package com.hariankoding.storyapp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hariankoding.storyapp.model.LoginResponse
import com.hariankoding.storyapp.model.Response
import com.hariankoding.storyapp.remote.Repository
import com.hariankoding.storyapp.utils.Result
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class LoginViewModel(private val repository: Repository) : ViewModel() {
    private var _loginResponse = MutableLiveData<Result<LoginResponse>>()
    val loginResponse: LiveData<Result<LoginResponse>> get() = _loginResponse

    fun login(email: String, password: String) {
        _loginResponse.postValue(Result.Loading)
        viewModelScope.launch {
            try {
                val data = repository.login(email, password)
                _loginResponse.postValue(Result.Success(data))
            } catch (e: Exception) {
                when (e) {
                    is IOException -> {
                        _loginResponse.postValue(
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
                        _loginResponse.postValue(
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