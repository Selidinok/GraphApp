package com.pointer.repositories.mappers

import com.pointer.remote.cashback.MerchantResponse
import io.kotlintest.assertSoftly
import io.kotlintest.properties.Gen
import io.kotlintest.shouldBe
import io.kotlintest.specs.FreeSpec

class MappersExtTest : FreeSpec() {

    private val randomString: String
        get() = Gen.string().nextPrintableString(5)

    init {
        "toMerchant" - {
            "All field should match" {
                val response = MerchantResponse(randomString, randomString, randomString, randomString, randomString)
                val merchant = response.toMerchant()
                assertSoftly {
                    merchant.affiliateUrl shouldBe response.affiliateUrl
                    merchant.id shouldBe response.merchantId
                    merchant.domain shouldBe response.merchantSite
                    merchant.title shouldBe response.label
                    merchant.subtitle shouldBe response.cashbackLabel
                }
            }
        }
    }

}