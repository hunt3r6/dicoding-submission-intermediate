package com.hariankoding.storyapp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hariankoding.storyapp.model.LoginResponse
import com.hariankoding.storyapp.remote.Repository
import com.hariankoding.storyapp.utils.Result
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: Repository) : ViewModel() {
    private var _loginResponse = MutableLiveData<Result<LoginResponse>>()
    val loginResponse: LiveData<Result<LoginResponse>> get() = _loginResponse

    fun login(email: String, password: String) {
        _loginResponse.postValue(Result.Loading)
        viewModelScope.launch {
            try {
                val data = repository.login(email, password)
                _loginResponse.postValue(Result.Success(data))
            } catch (e: Throwable) {
                _loginResponse.postValue(Result.Error(e.toString()))

            }
        }
    }

}