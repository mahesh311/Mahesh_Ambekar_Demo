package com.mahesh.demo.data.remote.datasources

import com.mahesh.demo.data.api.ApiService
import com.mahesh.demo.data.entities.holdings.GetHoldingsResponse
import retrofit2.Response
import javax.inject.Inject

class HoldingsRemoteDataSourceImpl @Inject constructor(val apiService: ApiService) :
    HoldingsRemoteDataSource {

    override suspend fun getHoldings(): Response<GetHoldingsResponse> {
        return apiService.getHoldings()
    }
}