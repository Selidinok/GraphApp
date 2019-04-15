package com.example.graphapp.remote

import io.kotlintest.properties.Gen
import java.time.ZoneOffset
import java.util.*

object RandomGenerator {
    val randomString: String
        get() = Gen.string().random().first()

    val randomInt: Int
        get() = Gen.int().random().first()

    val randomLong: Long
        get() = Gen.long().random().first()

    val randomDate: Date
        get() = Date.from(Gen.localDateTime(2018, 2020).random().first().toInstant(ZoneOffset.UTC))
}