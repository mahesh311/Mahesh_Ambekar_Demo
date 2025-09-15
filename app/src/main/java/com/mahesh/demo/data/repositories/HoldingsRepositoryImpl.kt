package com.mahesh.demo.data.repositories

import com.mahesh.demo.data.api.NetworkConstant
import com.mahesh.demo.data.remote.datasources.HoldingsRemoteDataSource
import com.mahesh.demo.data.utils.ApiResponse
import com.mahesh.demo.domain.mapper.toHoldings
import com.mahesh.demo.domain.repositories.HoldingsRepository
import com.mahesh.demo.presentation.entities.Holdings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.net.ConnectException
import java.net.SocketTimeoutException
import javax.inject.Inject

class HoldingsRepositoryImpl @Inject constructor(val holdingsRemoteDataSource: HoldingsRemoteDataSource) :
    HoldingsRepository {

    override suspend fun getHoldings(): Flow<ApiResponse<List<Holdings>>> = flow {
        emit(ApiResponse.Loading())
        var apiResponse: ApiResponse<List<Holdings>>

        try {
            val response = holdingsRemoteDataSource.getHoldings()
            if (response.isSuccessful) {
                apiResponse = response.body()?.let { body ->
                    ApiResponse.Success(body.toHoldings())
                } ?: ApiResponse.Error(
                    errorMessage = NetworkConstant.SOMETHING_WENT_WRONG,
                    errorCode = 500
                )
            } else {
                val errorMessage = response.errorBody()?.string() ?: ""
                apiResponse = ApiResponse.Error(errorMessage, response.code())
            }
        } catch (exception: Exception) {
            when (exception) {
                is ConnectException,
                is SocketTimeoutException,
                    -> {
                    apiResponse = ApiResponse.Error(
                        errorMessage = NetworkConstant.CONNECTION_ERROR,
                        errorCode = 1001
                    )
                }

                else -> {
                    apiResponse = ApiResponse.Error(
                        errorMessage = NetworkConstant.SOMETHING_WENT_WRONG,
                        errorCode = 500
                    )
                }
            }
        }

        emit(apiResponse)
    }
}