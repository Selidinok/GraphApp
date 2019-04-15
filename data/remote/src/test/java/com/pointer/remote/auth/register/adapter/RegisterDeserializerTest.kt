package com.example.graphapp.remote.register.adapter

import com.google.gson.JsonElement
import io.kotlintest.specs.FreeSpec
import io.kotlintest.shouldBe
import io.mockk.every
import io.mockk.mockk

class RegisterDeserializerTest : FreeSpec() {

    private val json = mockk<JsonElement>()

    override fun beforeProject() {
        super.beforeProject()
    }

    init {
    }

}