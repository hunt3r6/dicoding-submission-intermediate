package com.hariankoding.storyapp.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.hariankoding.storyapp.databinding.ActivityLoginBinding
import com.hariankoding.storyapp.ui.register.RegisterActivity
import com.hariankoding.storyapp.utils.Result
import com.hariankoding.storyapp.utils.SharedPreferencesHelper
import com.hariankoding.storyapp.viewmodel.ViewModelFactory

class LoginActivity : AppCompatActivity() {
    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private val prefHelper by lazy {
        SharedPreferencesHelper.invoke(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setOnClick()
        setObserver()
    }

    private fun setObserver() {
        viewModel.loginResponse.observe(this) { result ->
            when (result) {
                is Result.Loading -> {

                }
                is Result.Success -> {
                    result.data.let {
                        Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    }
                }
                is Result.Error -> {

                }
            }

        }
    }

    private fun setOnClick() {
        binding.btnLogin.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()
            viewModel.login(email, password)
        }

        binding.btnRegister.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

}