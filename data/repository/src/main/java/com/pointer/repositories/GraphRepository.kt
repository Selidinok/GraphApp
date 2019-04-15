package com.pointer.repositories

import com.example.graphapp.remote.GraphRemoteSource
import com.example.model.domain.Point
import com.example.model.remote.GraphResponse
import com.github.kittinunf.result.coroutines.SuspendableResult
import com.pointer.core.NetworkProvider
import com.pointer.core.base.AppExceptions
import com.pointer.repositories.mappers.toPointList

class GraphRepository(
    private val graphRemoteSource: GraphRemoteSource,
    private val networkProvider: NetworkProvider
) {
    suspend fun getPoints(count: Int): SuspendableResult<List<Point>, AppExceptions> {
        return SuspendableResult.of {
            if (!networkProvider.isConnected) throw AppExceptions.NetworkOffException()
            try {
                val response = graphRemoteSource.getPoints(count)
                if (response is GraphResponse.SuccessResponse) {
                    response.toPointList()
                } else {
                    throw AppExceptions.GraphException((response as GraphResponse.FailResponse).fail.message)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                throw AppExceptions.NetworkException(e.message ?: "")
            }
        }

    }
}