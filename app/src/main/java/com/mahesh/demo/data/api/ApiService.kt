package com.mahesh.demo.data.api

import com.mahesh.demo.data.entities.holdings.GetHoldingsResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET(NetworkConstant.GET_HOLDINGS)
    suspend fun getHoldings(): Response<GetHoldingsResponse>
}