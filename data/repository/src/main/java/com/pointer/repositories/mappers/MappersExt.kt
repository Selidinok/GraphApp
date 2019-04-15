package com.pointer.repositories.mappers

import com.example.model.domain.Point
import com.example.model.remote.GraphResponse
import com.example.model.remote.PointResponse

fun GraphResponse.SuccessResponse.toPointList() = points.pointResponses.map { it.toPoint() }

fun PointResponse.toPoint() = Point(x, y)