package com.hariankoding.storyapp.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.hariankoding.storyapp.data.database.StoryDatabase
import com.hariankoding.storyapp.data.network.ApiService
import com.hariankoding.storyapp.utils.DataDummy
import com.hariankoding.storyapp.utils.MainDispatcherRule
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
class RepositoryTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var apiService: ApiService

    @Mock
    private lateinit var storyDatabase: StoryDatabase

    @Mock
    private lateinit var repository: Repository
    private val dummyMultipart = DataDummy.generateDummyMultipartFile()
    private val dummyDescription = DataDummy.generateDummyRequestBody()

    private val dummyName = "Faisal Mahadi"
    private val dummyPassword = "password"
    private val dummyEmail = "faisalmahadi126@gmail.com"

    @Before
    fun setUp() {
        repository = Repository.getInstance(apiService, storyDatabase)
    }

    @Test
    fun `login success`() = runTest {
        val expectedLoginResponse = DataDummy.generateResponseLogin()
        `when`(apiService.login(dummyEmail, dummyPassword)).thenReturn(expectedLoginResponse)
        val actualLogin = apiService.login(dummyEmail, dummyPassword)
        verify(apiService).login(dummyEmail, dummyPassword)
        assertFalse(actualLogin.error)
        assertEquals(actualLogin.message, "success")
    }

    @Test
    fun `register success`() = runTest {
        val expectedRegisterResponse = DataDummy.generateResponseRegister()
        `when`(apiService.register(dummyName, dummyEmail, dummyPassword)).thenReturn(
            expectedRegisterResponse
        )
        val actualRegister = apiService.register(dummyName, dummyEmail, dummyPassword)
        verify(apiService).register(dummyName, dummyEmail, dummyPassword)
        assertEquals(actualRegister.message, "success")
        assertFalse(actualRegister.error)
    }

    @Test
    fun `get List Story`() = runTest {
        val expectedListStory = DataDummy.generateDummyLocationResponse()
        `when`(apiService.allStories()).thenReturn(expectedListStory)
        val actualListStory = apiService.allStories()
        verify(apiService).allStories()
        assertNotNull(actualListStory)
        assertEquals(actualListStory.listStory.size, expectedListStory.listStory.size)
    }

    @Test
    fun `upload story`() = runTest {
        val expectedUpload = DataDummy.generateUploadSuccess()
        `when`(
            apiService.uploadStories(
                dummyMultipart,
                dummyDescription
            )
        ).thenReturn(expectedUpload)
        val actualUpload = apiService.uploadStories(dummyMultipart, dummyDescription)
        verify(apiService).uploadStories(dummyMultipart, dummyDescription)
        assertFalse(actualUpload.error)
        assertEquals(actualUpload.message, expectedUpload.message)
    }
}