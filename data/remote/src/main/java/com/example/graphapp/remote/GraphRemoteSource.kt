package com.example.graphapp.remote

class GraphRemoteSource(
    private val api: GraphApi
) {

    suspend fun getPoints(count: Int) = api.getPoints(count).await()
}