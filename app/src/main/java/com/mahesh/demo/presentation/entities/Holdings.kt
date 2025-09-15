package com.mahesh.demo.presentation.entities

data class Holdings(
    val symbol: String,
    val quantity: Int,
    val ltp: Double,
    val avgPrice: Double,
    val close: Double,
    val totalPnl : Double,
    val todayPnl : Double
)