package com.pointer.repositories

import android.accounts.NetworkErrorException
import android.content.Context
import com.pointer.cache.prefs.AuthPrefs
import com.pointer.cache.prefs.prefsModule
import com.pointer.core.NetworkProvider
import com.pointer.core.base.AppExceptions
import com.pointer.remote.auth.AuthRemoteSource
import com.pointer.remote.auth.login.entity.JwtResponse
import com.pointer.remote.auth.register.entity.RegisterRequest
import com.pointer.remote.auth.register.entity.RegisterResponse
import com.pointer.remote.auth.remoteAuthModule
import com.pointer.repositories.RandomGenerator.randomDate
import com.pointer.repositories.RandomGenerator.randomInt
import com.pointer.repositories.RandomGenerator.randomString
import com.pointer.repositories.di.repositoriesModule
import io.kotlintest.Spec
import io.kotlintest.assertSoftly
import io.kotlintest.shouldThrow
import io.kotlintest.specs.FreeSpec
import io.mockk.*
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext
import org.koin.standalone.inject
import org.koin.test.KoinTest
import org.koin.test.declare

class AuthRepositoryTest : FreeSpec(), KoinTest {
    private val networkProvider = mockk<NetworkProvider>()
    private val authRemote = mockk<AuthRemoteSource>()
    private val authPrefs = mockk<AuthPrefs>(relaxed = true)
    private val repository by inject<AuthRepository>()
    private val context = mockk<Context>()

    override fun beforeSpec(spec: Spec) {
        super.beforeSpec(spec)
        val networkModule = module {
            single { networkProvider }
            single { context }
        }
        StandAloneContext.startKoin(listOf(repositoriesModule, prefsModule, remoteAuthModule, networkModule))
        declare {
            single(override = true) { authRemote }
            single(override = true) { authPrefs }
        }
    }

    init {
        "isFirstLaunch" - {
            "should return true" {
                every { authPrefs.isFirstLaunch } returns true
                repository.isFirstLaunch shouldBe true
                verify { authPrefs.isFirstLaunch }
            }
            "should return false" {
                every { authPrefs.isFirstLaunch } returns false
                repository.isFirstLaunch shouldBe false
                verify { authPrefs.isFirstLaunch }
            }
        }

        "userName" - {
            "should return name from prefs" {
                val name = randomString
                every { authPrefs.userName } returns name
                repository.userName shouldBe name
                verify { authPrefs.userName }
            }
        }

        "userId" - {
            "should return id from prefs" {
                val id = randomString
                every { authPrefs.userId } returns id
                repository.userId shouldBe id
                verify { authPrefs.userId }
            }
        }

        "expirationDate" - {
            "should return date from prefs" {
                val date = randomDate
                every { authPrefs.expirationDate } returns date
                repository.expirationDate shouldBe date
                verify { authPrefs.expirationDate }
            }
        }

        "login" - {
            val response = JwtResponse(randomString, randomDate, randomDate, randomString, null, null, null)
            val name = randomString
            coEvery { authRemote.getJwtResponse(any()) } returns response
            every { networkProvider.isConnected } returns true
            val result = repository.login(name)

            "should return success with id" {
                assertSoftly {
                    result.get().shouldBeInstanceOf<String>()
                    result.get().shouldNotBeEmpty()
                }
            }

            "should set id, name and exp date" {
                verify { authPrefs.userName = name }
                verify { authPrefs.userId = response.id }
                verify { authPrefs.expirationDate = response.exp }
            }

            "should throw NetworkOffException" {
                every { networkProvider.isConnected } returns false
                val errorResult = repository.login(name)
                shouldThrow<AppExceptions.NetworkOffException> {
                    errorResult.get()
                }
            }

            "should throw GraphException" {
                every { networkProvider.isConnected } returns true
                val errorResponse = JwtResponse(randomString, randomDate, randomDate, "0", randomInt, randomString, randomString)
                coEvery { authRemote.getJwtResponse(any()) } returns errorResponse
                val errorResult = repository.login(name)
                shouldThrow<AppExceptions.GraphException> { errorResult.get() }
            }
        }

        "register" - {
            val successResponse = RegisterResponse.SuccessResponse(randomString, randomString, randomDate)
            every { networkProvider.isConnected } returns true
            val request = RegisterRequest("test", "test", "test", "test")
            coEvery { authRemote.getRegisterResponse(any()) } returns successResponse
            val result = repository.register(request)

            "should return success pointResponses" {
                assertSoftly {
                    result.get().shouldBeInstanceOf<RegisterResponse.SuccessResponse>()
                    result.get().expirationDate.shouldNotBeNull()
                    result.get().name.shouldNotBeEmpty()
                    result.get().id.shouldNotBeEmpty()
                }
            }

            "should set id, name and exp date" {
                verify { authPrefs.userName = result.get().name }
                verify { authPrefs.userId = result.get().id }
                verify { authPrefs.expirationDate = result.get().expirationDate }
            }

            "should throw NetworkOffException" {
                every { networkProvider.isConnected } returns false
                val errorResult = repository.register(request)
                shouldThrow<AppExceptions.NetworkOffException> {
                    errorResult.get()
                }
            }

            "should throw GraphException" {
                every { networkProvider.isConnected } returns true
                val failFailResponse = RegisterResponse.FailResponse(5, randomString, randomString)
                coEvery { authRemote.getRegisterResponse(any()) } returns failFailResponse
                val errorResult = repository.register(request)
                shouldThrow<AppExceptions.GraphException> { errorResult.get() }
            }

            "should throw RequestException" {
                coEvery { authRemote.getRegisterResponse(any()) } throws NetworkErrorException("some exception")
                val errorResult = repository.register(request)
                shouldThrow<AppExceptions.RequestException> { errorResult.get() }
            }
        }
    }
}