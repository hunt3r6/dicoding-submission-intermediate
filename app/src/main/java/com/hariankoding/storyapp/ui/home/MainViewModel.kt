package com.hariankoding.storyapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.hariankoding.storyapp.data.Repository
import com.hariankoding.storyapp.data.database.entity.ListStoryEntity

class MainViewModel(private val repository: Repository) : ViewModel() {
    val listStoryResponse: LiveData<PagingData<ListStoryEntity>> =
        repository.allStories().cachedIn(viewModelScope)
}