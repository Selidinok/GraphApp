package com.example.graphapp

import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.example.graphapp.di.appComponent

import org.junit.Test
import org.junit.runner.RunWith

import com.example.graphapp.remote.GraphRemoteSource
import com.example.model.remote.GraphResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.koin.android.ext.koin.with
import org.koin.standalone.StandAloneContext
import org.koin.standalone.StandAloneContext.startKoin
import org.koin.standalone.inject
import org.koin.test.KoinTest
import timber.log.Timber

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
@MediumTest
class GraphRemoteSourceTest : KoinTest {
    private val remote by inject<GraphRemoteSource>()

    @Before
    fun setUp() {
//        startKoin(
//            appComponent
//        ) with InstrumentationRegistry.getInstrumentation().context
    }

    @After
    fun close() {
        StandAloneContext.stopKoin()
    }

    @Test
    fun responseShouldBeSuccess() = runBlocking {
        val count = 3
        val response = remote.getPoints(count)
        Timber.d("Response = $response")
        assert(response is GraphResponse.SuccessResponse)
        assert((response as GraphResponse.SuccessResponse).points.pointResponses.size == count)
    }
}
