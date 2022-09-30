package com.hariankoding.storyapp.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.hariankoding.storyapp.R
import com.hariankoding.storyapp.ui.home.MainActivity
import com.hariankoding.storyapp.ui.login.LoginActivity
import com.hariankoding.storyapp.utils.SharedPreferencesHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    private val prefHelper by lazy {
        SharedPreferencesHelper.invoke(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        val token = prefHelper.fetchAuthToken()
        lifecycleScope.launch {
            delay(1000)
            if (token.isNullOrEmpty()) {
                startActivity(Intent(this@SplashScreenActivity, LoginActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
                finish()
            }
        }
    }
}