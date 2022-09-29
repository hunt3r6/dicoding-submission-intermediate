package com.hariankoding.storyapp.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

class SharedPreferencesHelper {
    companion object {
        const val USER_TOKEN = "user_token"
        private var prefs: SharedPreferences? = null

        @Volatile
        private var instance: SharedPreferencesHelper? = null
        private val LOCK = Any()

        operator fun invoke(context: Context): SharedPreferencesHelper =
            instance ?: synchronized(LOCK) {
                instance ?: buildHelper(context).also {
                    instance = it

                }
            }

        private fun buildHelper(context: Context): SharedPreferencesHelper {
            val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
            val mainKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)
            val sharePrefFile = "sharedPref"
            prefs = EncryptedSharedPreferences.create(
                sharePrefFile,
                mainKeyAlias,
                context,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
            return SharedPreferencesHelper()
        }

    }

    /**
     * Function to save auth token
     */
    fun saveAuthToken(token: String) {
        prefs?.edit(commit = true) {
            putString(USER_TOKEN, token)
        }
    }

    fun fetchAuthToken(): String? {
        return prefs?.getString(USER_TOKEN, "")
    }

}