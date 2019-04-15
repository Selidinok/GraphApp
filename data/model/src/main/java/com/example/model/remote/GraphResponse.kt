package com.example.model.remote

import com.google.gson.annotations.SerializedName

sealed class GraphResponse {
    data class SuccessResponse(
        @SerializedName("response")
        val points: Points,
        @SerializedName("result")
        val result: Int
    ) : GraphResponse()

    data class FailResponse(
        @SerializedName("response")
        val fail: Fail
    ) : GraphResponse()
}

data class PointResponse(
    @SerializedName("x")
    val x: String,
    @SerializedName("y")
    val y: String
)

data class Points(
    @SerializedName("points")
    val pointResponses: List<PointResponse>
)

data class Fail(
    @SerializedName("message")
    val message: String,
    @SerializedName("result")
    val result: Int
)