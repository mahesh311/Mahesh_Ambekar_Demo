package com.mahesh.demo.domain.usecases

import com.mahesh.demo.data.utils.ApiResponse
import com.mahesh.demo.domain.repositories.HoldingsRepository
import com.mahesh.demo.presentation.entities.Holdings
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HoldingsUseCase @Inject constructor(private val holdingsRepository: HoldingsRepository) {
    suspend fun getHoldings(): Flow<ApiResponse<List<Holdings>>> {
        return holdingsRepository.getHoldings()
    }
}