package com.hariankoding.storyapp.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.hariankoding.storyapp.data.Repository
import com.hariankoding.storyapp.data.network.model.LoginResponse
import com.hariankoding.storyapp.utils.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: Repository
    private lateinit var loginViewModel: LoginViewModel
    private val dummyLoginResponse = DataDummy.generateResponseLogin()
    private val dummyEmail = "faisal@mail.com"
    private val dummyPassword = "password"


    @Before
    fun setUp() {
        loginViewModel = LoginViewModel(repository)
    }

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `when login success return Result Success`() = runTest {
        val expectedLogin = MutableLiveData<Result<LoginResponse>>()
        expectedLogin.value = Result.Success(dummyLoginResponse)

        Mockito.`when`(repository.login(dummyEmail, dummyPassword)).thenReturn(
            expectedLogin
        )
        val actualRegister = loginViewModel.login(dummyEmail, dummyPassword).getOrAwaitValue {}
        Mockito.verify(repository).login(dummyEmail, dummyPassword)
        assertTrue(actualRegister is Result.Success)

    }

    @Test
    fun `when login failed return Result Error`() = runTest {
        val expectedLogin = MutableLiveData<Result<LoginResponse>>()
        expectedLogin.value = Result.Error("error")

        Mockito.`when`(repository.login(dummyEmail, dummyPassword)).thenReturn(
            expectedLogin
        )
        val actualRegister = loginViewModel.login(dummyEmail, dummyPassword).getOrAwaitValue {}
        Mockito.verify(repository).login(dummyEmail, dummyPassword)
        assertTrue(actualRegister is Result.Error)

    }

}