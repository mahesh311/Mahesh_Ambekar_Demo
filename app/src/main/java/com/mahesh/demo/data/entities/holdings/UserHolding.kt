package com.mahesh.demo.data.entities.holdings

import com.mahesh.demo.presentation.entities.Holdings

data class UserHolding(
    val symbol: String?,
    val quantity: Int?,
    val ltp: Double?,
    val avgPrice: Double?,
    val close: Double?
)

fun UserHolding.toHoldings(): Holdings {
    return Holdings(
        symbol = symbol ?: "",
        quantity = quantity ?: 0,
        ltp = ltp ?: 0.0,
        avgPrice = avgPrice ?: 0.0,
        close = close ?: 0.0,
        totalPnl = ((ltp ?: 0.0) * (quantity ?: 0)) - ((avgPrice ?: 0.0) * (quantity ?: 0)),
        todayPnl = 0.0
    )
}