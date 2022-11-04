package com.hariankoding.storyapp.ui.createstory

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.hariankoding.storyapp.data.Repository
import com.hariankoding.storyapp.data.network.model.Response
import com.hariankoding.storyapp.utils.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class CreateStoryViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: Repository
    private lateinit var createStoryViewModel: CreateStoryViewModel

    private val dummyUploadSuccess = DataDummy.generateUploadSuccess()
    private val dummyMultipart = DataDummy.generateDummyMultipartFile()
    private val dummyDescription = DataDummy.generateDummyRequestBody()

    @Before
    fun setUp() {
        createStoryViewModel = CreateStoryViewModel(repository)
    }

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `when upload success return result Success`() = runTest {
        val expectedUploadResponse = MutableLiveData<Result<Response>>()
        expectedUploadResponse.value = Result.Success(dummyUploadSuccess)
        `when`(repository.uploadStories(dummyMultipart, dummyDescription)).thenReturn(
            expectedUploadResponse
        )
        val actualUploadResponse = createStoryViewModel.uploadImage(dummyMultipart,dummyDescription).getOrAwaitValue {  }
        verify(repository).uploadStories(dummyMultipart, dummyDescription)
        assertTrue(actualUploadResponse is Result.Success)
    }

    @Test
    fun `when upload failed return result error`() = runTest {
        val expectedUploadResponse = MutableLiveData<Result<Response>>()
        expectedUploadResponse.value = Result.Error("error")
        `when`(repository.uploadStories(dummyMultipart, dummyDescription)).thenReturn(
            expectedUploadResponse
        )
        val actualUploadResponse = createStoryViewModel.uploadImage(dummyMultipart,dummyDescription).getOrAwaitValue {  }
        verify(repository).uploadStories(dummyMultipart, dummyDescription)
        assertTrue(actualUploadResponse is Result.Error)
    }


}