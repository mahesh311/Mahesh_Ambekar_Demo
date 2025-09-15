package com.mahesh.demo.data.repositories

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.mahesh.demo.data.api.NetworkConstant
import com.mahesh.demo.data.entities.holdings.Data
import com.mahesh.demo.data.entities.holdings.GetHoldingsResponse
import com.mahesh.demo.data.entities.holdings.UserHolding
import com.mahesh.demo.data.remote.datasources.HoldingsRemoteDataSource
import com.mahesh.demo.data.utils.ApiResponse
import com.mahesh.demo.domain.mapper.toHoldings
import com.mahesh.demo.presentation.entities.Holdings
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test
import retrofit2.Response
import java.net.ConnectException
import java.net.SocketTimeoutException

@OptIn(ExperimentalCoroutinesApi::class)
class HoldingsRepositoryImplTest {

    private val remoteDataSource: HoldingsRemoteDataSource = mockk()
    private val repository = HoldingsRepositoryImpl(remoteDataSource)

    @Test
    fun `getHoldings successful response with data`() = runTest {
        val dto = GetHoldingsResponse(
            data = Data(
                listOf(
                    UserHolding(
                        symbol = "ABC",
                        quantity = 10,
                        ltp = 100.0,
                        close = 250.00,
                        avgPrice = 200.0
                    )
                )
            )
        )
        coEvery { remoteDataSource.getHoldings() } returns Response.success(dto)

        repository.getHoldings().test {
            assertThat(awaitItem()).isInstanceOf(ApiResponse.Loading::class.java)
            val success = awaitItem() as ApiResponse.Success
            assertThat(success.data).isEqualTo(dto.toHoldings())
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `getHoldings successful response with null body`() = runTest {
        coEvery { remoteDataSource.getHoldings() } returns Response.success(null)

        repository.getHoldings().test {
            assertThat(awaitItem()).isInstanceOf(ApiResponse.Loading::class.java)
            val error = awaitItem() as ApiResponse.Error
            assertThat(error.errorMessage).isEqualTo(NetworkConstant.SOMETHING_WENT_WRONG)
            assertThat(error.errorCode).isEqualTo(500)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `getHoldings error response with error body`() = runTest {
        val errorBody = "server error".toResponseBody("text/plain".toMediaType())
        coEvery { remoteDataSource.getHoldings() } returns Response.error(400, errorBody)

        repository.getHoldings().test {
            assertThat(awaitItem()).isInstanceOf(ApiResponse.Loading::class.java)
            val error = awaitItem() as ApiResponse.Error
            assertThat(error.errorMessage).contains("server error")
            assertThat(error.errorCode).isEqualTo(400)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `getHoldings error response with no error body`() = runTest {
        coEvery { remoteDataSource.getHoldings() } returns Response.error(
            404,
            "".toResponseBody("text/plain".toMediaType())
        )

        repository.getHoldings().test {
            assertThat(awaitItem()).isInstanceOf(ApiResponse.Loading::class.java)
            val error = awaitItem() as ApiResponse.Error
            assertThat(error.errorMessage).isEqualTo("")
            assertThat(error.errorCode).isEqualTo(404)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `getHoldings ConnectException`() = runTest {
        coEvery { remoteDataSource.getHoldings() } coAnswers {
            throw ConnectException("failed connect")
        }

        repository.getHoldings().test {
            assertThat(awaitItem()).isInstanceOf(ApiResponse.Loading::class.java)
            val error = awaitItem() as ApiResponse.Error
            assertThat(error.errorMessage).isEqualTo(NetworkConstant.CONNECTION_ERROR)
            assertThat(error.errorCode).isEqualTo(1001)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `getHoldings SocketTimeoutException`() = runTest {
        coEvery { remoteDataSource.getHoldings() } coAnswers {
            throw SocketTimeoutException("timeout")
        }

        repository.getHoldings().test {
            assertThat(awaitItem()).isInstanceOf(ApiResponse.Loading::class.java)
            val error = awaitItem() as ApiResponse.Error
            assertThat(error.errorMessage).isEqualTo(NetworkConstant.CONNECTION_ERROR)
            assertThat(error.errorCode).isEqualTo(1001)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `getHoldings http error response`() = runTest {
        val errorBody = "server down".toResponseBody("text/plain".toMediaType())
        coEvery { remoteDataSource.getHoldings() } returns Response.error(500, errorBody)

        repository.getHoldings().test {
            assertThat(awaitItem()).isInstanceOf(ApiResponse.Loading::class.java)
            val error = awaitItem() as ApiResponse.Error
            assertThat(error.errorMessage).contains("server down")
            assertThat(error.errorCode).isEqualTo(500)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `getHoldings emits loading state first`() = runTest {
        coEvery { remoteDataSource.getHoldings() } throws RuntimeException("boom")

        repository.getHoldings().test {
            val first = awaitItem()
            assertThat(first).isInstanceOf(ApiResponse.Loading::class.java)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `getHoldings emits only two states on success`() = runTest {
        val dto = GetHoldingsResponse(
            data = Data(
                listOf(
                    UserHolding(
                        symbol = "ABC",
                        quantity = 10,
                        ltp = 100.0,
                        close = 250.00,
                        avgPrice = 200.0
                    )
                )
            )
        )
        coEvery { remoteDataSource.getHoldings() } returns Response.success(dto)

        repository.getHoldings().test {
            val emissions = mutableListOf<ApiResponse<List<Holdings>>>()
            emissions.add(awaitItem())
            emissions.add(awaitItem())

            assertThat(emissions.size).isEqualTo(2)
            assertThat(emissions[0]).isInstanceOf(ApiResponse.Loading::class.java)
            assertThat(emissions[1]).isInstanceOf(ApiResponse.Success::class.java)

            val success = emissions[1] as ApiResponse.Success
            assertThat(success.data).isEqualTo(dto.toHoldings())

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `getHoldings emits only two states on failure`() = runTest {
        coEvery { remoteDataSource.getHoldings() } throws RuntimeException("boom")

        repository.getHoldings().test {
            val emissions = mutableListOf<ApiResponse<List<Holdings>>>()
            emissions.add(awaitItem())
            emissions.add(awaitItem())
            assertThat(emissions.size).isEqualTo(2)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `getHoldings with empty holdings list`() = runTest {
        val dto = GetHoldingsResponse(
            data = Data(emptyList())
        )

        coEvery { remoteDataSource.getHoldings() } returns Response.success(dto)

        repository.getHoldings().test {
            assertThat(awaitItem()).isInstanceOf(ApiResponse.Loading::class.java)
            val success = awaitItem() as ApiResponse.Success
            assertThat(success.data).isEmpty()
            cancelAndConsumeRemainingEvents()
        }
    }
}
