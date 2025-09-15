package com.mahesh.demo.domain.mapper

import com.mahesh.demo.data.entities.holdings.GetHoldingsResponse
import com.mahesh.demo.data.entities.holdings.toHoldings
import com.mahesh.demo.presentation.entities.Holdings

fun GetHoldingsResponse.toHoldings(): List<Holdings> {
    val holdingsList = mutableListOf<Holdings>()
    if (this.data != null && this.data.userHolding != null) {
        for (holdingResponse in this.data.userHolding) {
            holdingsList.add(holdingResponse.toHoldings())
        }
    }
    return holdingsList
}