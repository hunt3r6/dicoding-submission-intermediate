package com.hariankoding.storyapp.ui.location

import androidx.lifecycle.ViewModel
import com.hariankoding.storyapp.data.Repository

class LocationViewModel(private val repository: Repository) : ViewModel() {
    fun getLocation() = repository.locationStory()
}
