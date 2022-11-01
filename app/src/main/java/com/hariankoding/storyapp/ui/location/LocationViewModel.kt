package com.hariankoding.storyapp.ui.location

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hariankoding.storyapp.data.Repository
import com.hariankoding.storyapp.data.network.model.ListStoryItem
import kotlinx.coroutines.launch

class LocationViewModel(private val repository: Repository) : ViewModel() {
    private var _locationStory = MutableLiveData<List<ListStoryItem>>()
    val locationStory: LiveData<List<ListStoryItem>> get() = _locationStory
    init {
        getLocation()
    }
    private fun getLocation() {
        viewModelScope.launch {
            _locationStory.value = repository.locationStory().listStory
        }
    }
}