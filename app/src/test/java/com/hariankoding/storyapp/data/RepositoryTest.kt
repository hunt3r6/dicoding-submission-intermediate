package com.hariankoding.storyapp.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import com.hariankoding.storyapp.data.database.StoryDatabase
import com.hariankoding.storyapp.data.database.entity.ListStoryEntity
import com.hariankoding.storyapp.data.network.ApiService
import com.hariankoding.storyapp.data.network.model.ListStoryItem
import com.hariankoding.storyapp.ui.home.StoryAdapter
import com.hariankoding.storyapp.ui.home.StoryPagingSource
import com.hariankoding.storyapp.ui.home.noopListUpdateCallback
import com.hariankoding.storyapp.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
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
    private lateinit var repositoryMock: Repository

    @Mock
    private lateinit var repository: Repository
    private val dummyMultipart = DataDummy.generateDummyMultipartFile()
    private val dummyDescription = DataDummy.generateDummyRequestBody()
    private val dummyStoryItem = DataDummy.generateDummyLocationResponse()

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
        val actualLogin = repository.login(dummyEmail, dummyPassword)
        actualLogin.observeForTesting {
            assertEquals((actualLogin.value as Result.Success).data.message, "success")
            assertFalse((actualLogin.value as Result.Success).data.error)
            assertEquals(expectedLoginResponse, (actualLogin.value as Result.Success).data)
        }
    }

    @Test
    fun `register success`() = runTest {
        val expectedRegisterResponse = DataDummy.generateResponseRegister()
        `when`(apiService.register(dummyName, dummyEmail, dummyPassword)).thenReturn(
            expectedRegisterResponse
        )
        val actualRegister = repository.register(dummyName, dummyEmail, dummyPassword)
        actualRegister.observeForTesting {
            assertEquals((actualRegister.value as Result.Success).data.message, "success")
            assertFalse((actualRegister.value as Result.Success).data.error)
        }

    }

    @Test
    fun `get List Story Paging`() = runTest {
        val dummyStory = DataDummy.generateDummyStoryResponse()
        val data: PagingData<ListStoryEntity> = StoryPagingSource.snapshot(dummyStory)
        val expectedStory = MutableLiveData<PagingData<ListStoryEntity>>()
        expectedStory.value = data
        `when`(repositoryMock.allStories()).thenReturn(expectedStory)
        val actualStory = repositoryMock.allStories().getOrAwaitValue { }
        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.diffCallback,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualStory)
        assertNotNull(differ.snapshot())
        assertEquals(dummyStory, differ.snapshot())
        assertEquals(dummyStory.size, differ.snapshot().size)
        assertEquals(dummyStory[0].id, differ.snapshot()[0]?.id)

    }

    @Test
    fun `get list Story Location`() = runTest {
        val expectedLocation = MutableLiveData<Result<List<ListStoryItem>>>()
        expectedLocation.value = Result.Success(dummyStoryItem.listStory)
        `when`(repositoryMock.locationStory()).thenReturn(expectedLocation)
        val actualStoryLocation = repositoryMock.locationStory().getOrAwaitValue()
        assertNotNull(actualStoryLocation)
        assertTrue(actualStoryLocation is Result.Success)
        assertEquals(dummyStoryItem.listStory, (actualStoryLocation as Result.Success).data)
        assertEquals(
            dummyStoryItem.listStory.size,
            actualStoryLocation.data.size
        )
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
        val actualUpload = repository.uploadStories(dummyMultipart, dummyDescription)
        actualUpload.observeForTesting {
            assertFalse((actualUpload.value as Result.Success).data.error)
            assertEquals(
                (actualUpload.value as Result.Success).data.message,
                expectedUpload.message
            )
        }

    }
}