package com.hariankoding.storyapp.data.network.model

import com.google.gson.annotations.SerializedName

data class Response(
    @field:SerializedName("error")
    val error: Boolean,
    @field:SerializedName("message")
    val message: String
)
