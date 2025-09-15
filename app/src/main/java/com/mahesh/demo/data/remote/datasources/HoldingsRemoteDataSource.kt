package com.mahesh.demo.data.remote.datasources

import com.mahesh.demo.data.entities.holdings.GetHoldingsResponse
import retrofit2.Response

interface HoldingsRemoteDataSource {
    suspend fun getHoldings() : Response<GetHoldingsResponse>
}