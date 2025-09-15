package com.mahesh.demo.domain.repositories

import com.mahesh.demo.data.utils.ApiResponse
import com.mahesh.demo.presentation.entities.Holdings
import kotlinx.coroutines.flow.Flow

interface HoldingsRepository {
    suspend fun getHoldings(): Flow<ApiResponse<List<Holdings>>>
}