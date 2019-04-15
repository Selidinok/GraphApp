package com.pointer.remote.auth

import com.example.graphapp.remote.GraphRemoteSource
import com.example.graphapp.remote.remoteModule
import com.example.model.remote.GraphResponse
import io.kotlintest.Spec
import io.kotlintest.assertSoftly
import io.kotlintest.matchers.types.shouldBeInstanceOf
import io.kotlintest.shouldBe
import io.kotlintest.specs.FreeSpec
import org.koin.standalone.StandAloneContext
import org.koin.standalone.StandAloneContext.startKoin
import org.koin.standalone.inject
import org.koin.test.KoinTest
import kotlin.random.Random

class GraphRemoteSourceTest : FreeSpec(), KoinTest {
    private val remote by inject<GraphRemoteSource>()
    private val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

    override fun beforeSpec(spec: Spec) {
        super.beforeSpec(spec)
        startKoin(listOf(remoteModule))
    }

    override fun afterSpec(spec: Spec) {
        super.afterSpec(spec)
        StandAloneContext.stopKoin()
    }

    private fun randomStringName(): String {
        return (0..8)
            .map { Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
    }

    init {
        "getPoints" - {
            "should pass with success" {
                val count = 3
                val response = remote.getPoints(count)
                println("Response = $response")
                assertSoftly {
                    response.shouldBeInstanceOf<GraphResponse.SuccessResponse>()
                    (response as GraphResponse.SuccessResponse).points.pointResponses.size shouldBe count
                }
            }
        }
//
//        "getRegisterResponse" - {
//            "should pass with success" {
//                val nickname = randomStringName()
//                val email = "$nickname@prova.pointer.it"
//                val request = RegisterRequest(randomString, randomString, email, "somepassword")
//                val response = remote.getRegisterResponse(request)
//                println("$response")
//                assertSoftly {
//                    response.shouldBeInstanceOf<RegisterResponse.SuccessResponse>()
//                    (response as RegisterResponse.SuccessResponse).name.shouldNotBeEmpty()
//                    response.id.shouldNotBeEmpty()
//                    response.expirationDate.shouldNotBeNull()
//                }
//            }
//
//            "should return error" {
//                val nickname = randomStringName()
//                val email = "$nickname@prova.pointer.it"
//                val request = RegisterRequest(randomString, randomString, email, "")
//                val response = remote.getRegisterResponse(request)
//                println("$response")
//                assertSoftly {
//                    response.shouldBeInstanceOf<RegisterResponse.FailResponse>()
//                    (response as RegisterResponse.FailResponse).error.shouldNotBeEmpty()
//                    response.errorId.shouldNotBe(0)
//                    response.errorIt.shouldNotBeEmpty()
//                }
//            }
//    }
}

}