package com.hariankoding.storyapp.ui.location

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.hariankoding.storyapp.data.Repository
import com.hariankoding.storyapp.data.network.model.ListStoryItem
import com.hariankoding.storyapp.utils.DataDummy
import com.hariankoding.storyapp.utils.MainDispatcherRule
import com.hariankoding.storyapp.utils.Result
import com.hariankoding.storyapp.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class LocationViewModelTest {


    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: Repository
    private lateinit var locationViewModel: LocationViewModel
    private val dummyStoryItem = DataDummy.generateDummyLocationResponse()

    @Before
    fun setUp() {
        locationViewModel = LocationViewModel(repository)
    }

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `when get location story Should Not Null and Return Success`() = runTest {
        val expectedLocation = MutableLiveData<Result<List<ListStoryItem>>>()
        expectedLocation.value = Result.Success(dummyStoryItem.listStory)
        `when`(repository.locationStory()).thenReturn(expectedLocation)
        val actualStory = locationViewModel.getLocation().getOrAwaitValue()
        Mockito.verify(repository).locationStory()
        assertNotNull(actualStory)
        assertTrue(actualStory is Result.Success)
        assertEquals(
            dummyStoryItem.listStory.size,
            (actualStory as Result.Success).data.size
        )
    }

    @Test
    fun `when get location story network error Should Return Error`() {
        val expectedLocation = MutableLiveData<Result<List<ListStoryItem>>>()
        expectedLocation.value = Result.Error("error")
        `when`(repository.locationStory()).thenReturn(expectedLocation)
        val actualStory = locationViewModel.getLocation().getOrAwaitValue()
        Mockito.verify(repository).locationStory()
        assertNotNull(actualStory)
        assertTrue(actualStory is Result.Error)
    }

}