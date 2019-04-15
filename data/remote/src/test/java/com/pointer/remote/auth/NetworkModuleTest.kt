//package com.example.graphapp.remote
//
//import android.content.Context
//import com.pointer.core.consts.KoinExtra
//import io.kotlintest.TestCase
//import io.kotlintest.inspectors.forOne
//import io.kotlintest.matchers.types.shouldBeInstanceOf
//import io.kotlintest.specs.FreeSpec
//import io.mockk.mockk
//import okhttp3.OkHttpClient
//import okhttp3.logging.HttpLoggingInterceptor
//import org.koin.android.ext.koin.with
//import org.koin.core.KoinProperties
//import org.koin.dsl.module.module
//import org.koin.standalone.StandAloneContext.startKoin
//import org.koin.standalone.StandAloneContext.stopKoin
//import org.koin.standalone.get
//import org.koin.test.KoinTest
//import org.koin.test.checkModules
//
//
//class NetworkModuleTest : FreeSpec(), KoinTest {
//    private val mockContext = mockk<Context>()
//
//    override fun beforeTest(testCase: TestCase) {
//        stopKoin()
//        val properties = KoinProperties(extraProperties = mapOf(KoinExtra.DEBUG to false))
//        startKoin(listOf(remoteModule), properties = properties) with mockContext
//    }
//
//    init {
//        "Network module should be binding" {
//            val mockedContextModule = module { single { mockContext } }
//            checkModules(listOf(mockedContextModule, remoteModule))
//        }
//
//        "Test OkHttp client" - {
//            "Logging interceptor" - {
//
//                "should be added to client" {
//                    stopKoin()
//                    startKoin(listOf(remoteModule)) with mockContext
//
//                    val interceptors = get<OkHttpClient>().interceptors()
//                    interceptors.forOne {
//                        it.shouldBeInstanceOf<HttpLoggingInterceptor>()
//                    }
//                }
//            }
//        }
//
//        "Build config" - {
//
//            "Access key should not be empty" {
//                BuildConfig.ACCESS_KEY.shouldNotBeEmpty()
//            }
//
//            "Auth url should not be empty" {
//                BuildConfig.AUTH_URL.shouldNotBeEmpty()
//            }
//
//            "Secret key should not be empty" {
//                BuildConfig.JWT_SECRET.shouldNotBeEmpty()
//            }
//        }
//    }
//}