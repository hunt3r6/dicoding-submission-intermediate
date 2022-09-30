package com.hariankoding.storyapp.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.hariankoding.storyapp.databinding.ActivityLoginBinding
import com.hariankoding.storyapp.ui.home.MainActivity
import com.hariankoding.storyapp.ui.register.RegisterActivity
import com.hariankoding.storyapp.utils.Result
import com.hariankoding.storyapp.utils.SharedPreferencesHelper
import com.hariankoding.storyapp.utils.UtilsUi
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
                    binding.btnLogin.isEnabled = false
                    UtilsUi.showDialog(this)
                }
                is Result.Success -> {
                    UtilsUi.closeDialog()
                    binding.btnLogin.isEnabled = true
                    result.data.let {
                        if (!it.error) {
                            prefHelper.saveAuthToken(it.loginResult.token)
                            message(it.message)
                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            finish()
                        } else {
                            message(it.message)
                        }
                    }
                }
                is Result.Error -> {
                    UtilsUi.closeDialog()
                    binding.btnLogin.isEnabled = true
                    message(result.error)
                }
            }

        }
    }

    private fun message(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
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