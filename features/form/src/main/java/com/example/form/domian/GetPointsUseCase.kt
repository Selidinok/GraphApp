package com.example.form.domian

import com.example.model.domain.Point
import com.github.kittinunf.result.coroutines.SuspendableResult
import com.github.kittinunf.result.coroutines.map
import com.pointer.core.base.AppExceptions
import com.pointer.repositories.GraphRepository

class GetPointsUseCase(
    private val repository: GraphRepository
) {
    suspend operator fun invoke(count: Int): SuspendableResult<List<Point>, AppExceptions> {
        return repository.getPoints(count).map { points ->
            points.sortedBy { it.x.toFloat() }
        }
    }
}