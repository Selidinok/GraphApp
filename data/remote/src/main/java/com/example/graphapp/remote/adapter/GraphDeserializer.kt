package com.example.graphapp.remote.adapter

import com.example.model.remote.GraphResponse
import com.google.gson.*
import timber.log.Timber
import java.lang.reflect.Type

class GraphDeserializer : JsonDeserializer<GraphResponse> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): GraphResponse {
        Timber.d("Response = ${json.toString()}")
        requireNotNull(json) { "Response was null." }
        val obj = json.asJsonObject

        val result = obj["result"]

        return if (result != null) createSuccessResponse(json)
        else createFailResponse(json)
    }

    private fun createSuccessResponse(json: JsonElement): GraphResponse.SuccessResponse {
        return Gson().fromJson(json, GraphResponse.SuccessResponse::class.java)
    }

    private fun createFailResponse(json: JsonElement): GraphResponse.FailResponse {
        return Gson().fromJson(json, GraphResponse.FailResponse::class.java)
    }
}