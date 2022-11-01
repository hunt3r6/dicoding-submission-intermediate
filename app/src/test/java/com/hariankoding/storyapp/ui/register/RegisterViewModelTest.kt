package com.hariankoding.storyapp.ui.register

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.hariankoding.storyapp.data.Repository
import com.hariankoding.storyapp.data.network.model.Response
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
class RegisterViewModelTest {
    private lateinit var registerViewModel: RegisterViewModel

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: Repository

    private val dummyResponseRegister = DataDummy.generateResponseRegister()
    private val dummyName = "faisal"
    private val dummyEmail = "faisal@mail.com"
    private val dummyPassword = "password"

    @Before
    fun setUp() {
        registerViewModel = RegisterViewModel(repository)
    }

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `when register success return Result Success`() = runTest {
        val expectedRegister = MutableLiveData<Result<Response>>()
        expectedRegister.value = Result.Success(dummyResponseRegister)

        `when`(repository.register(dummyName, dummyEmail, dummyPassword)).thenReturn(
            expectedRegister
        )
        val actualRegister = registerViewModel.register(dummyName, dummyEmail, dummyPassword).getOrAwaitValue {}
        Mockito.verify(repository).register(dummyName, dummyEmail, dummyPassword)
        assertTrue(actualRegister is Result.Success)

    }

    @Test
    fun `when register failed return Result Error`() = runTest {
        val expectedRegister = MutableLiveData<Result<Response>>()
        expectedRegister.value = Result.Error("failed")

        `when`(repository.register(dummyName, dummyEmail, dummyPassword)).thenReturn(
            expectedRegister
        )
        val actualRegister = registerViewModel.register(dummyName, dummyEmail, dummyPassword).getOrAwaitValue()
        Mockito.verify(repository).register(dummyName, dummyEmail, dummyPassword)
        assertTrue(actualRegister is Result.Error)
    }

}