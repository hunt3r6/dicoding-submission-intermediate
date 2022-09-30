package com.hariankoding.storyapp.ui.register

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.hariankoding.storyapp.databinding.ActivityRegisterBinding
import com.hariankoding.storyapp.ui.login.LoginActivity
import com.hariankoding.storyapp.utils.Result
import com.hariankoding.storyapp.utils.UtilsUi
import com.hariankoding.storyapp.viewmodel.ViewModelFactory

class RegisterActivity : AppCompatActivity() {
    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityRegisterBinding.inflate(layoutInflater)
    }
    private val viewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setOnClick()
        setObserver()
    }

    private fun setObserver() {
        viewModel.registerResponse.observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.btnRegister.isEnabled = false
                    UtilsUi.showDialog(this)
                }
                is Result.Success -> {
                    UtilsUi.closeDialog()
                    binding.btnRegister.isEnabled = true
                    result.data.let {
                        message(it.message)
                        binding.edRegisterName.setText("")
                        binding.edRegisterEmail.setText("")
                        binding.edRegisterPassword.setText("")
                    }
                }
                is Result.Error -> {
                    UtilsUi.closeDialog()
                    binding.btnRegister.isEnabled = true
                    message(result.error)
                }
            }
        }
    }

    private fun message(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun setOnClick() = with(binding) {
        btnRegister.setOnClickListener {
            val name = edRegisterName.text.toString()
            val email = edRegisterEmail.text.toString()
            val password = edRegisterPassword.text.toString()
            viewModel.register(name, email, password)
        }
        btnLogin.setOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}