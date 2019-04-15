package com.example.graphapp.remote

import com.example.model.remote.GraphResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface GraphApi {

    @FormUrlEncoded
    @POST("pointsList")
    fun getPoints(@Field("count") count: Int): Deferred<GraphResponse>
}