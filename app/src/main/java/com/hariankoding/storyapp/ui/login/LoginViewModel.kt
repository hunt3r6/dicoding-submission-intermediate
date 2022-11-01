package com.hariankoding.storyapp.ui.login

import androidx.lifecycle.ViewModel
import com.hariankoding.storyapp.data.Repository

class LoginViewModel(private val repository: Repository) : ViewModel() {

    fun login(email: String, password: String) = repository.login(email, password)
}