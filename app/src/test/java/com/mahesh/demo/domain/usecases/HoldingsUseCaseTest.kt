package com.mahesh.demo.domain.usecases

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.mahesh.demo.data.utils.ApiResponse
import com.mahesh.demo.domain.repositories.HoldingsRepository
import com.mahesh.demo.presentation.entities.Holdings
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Test

class HoldingsUseCaseTest {

    private val repository = mockk<HoldingsRepository>(relaxed = true)
    private val useCase = HoldingsUseCase(repository)

    @Test
    fun `usecase forwards loading and success from repository`() = runTest {
        val sample = listOf(
            Holdings(
                symbol = "ABC",
                quantity = 10,
                ltp = 100.0,
                close = 250.00,
                avgPrice = 200.0,
                todayPnl = 1500.0,
                totalPnl = 1000.0
            )
        )

        val repoFlow = flow {
            emit(ApiResponse.Loading())
            emit(ApiResponse.Success(sample))
        }

        coEvery { repository.getHoldings() } returns repoFlow

        useCase.getHoldings().test {
            val first = awaitItem()
            assertThat(first).isInstanceOf(ApiResponse.Loading::class.java)

            val second = awaitItem()
            assertThat(second).isInstanceOf(ApiResponse.Success::class.java)
            val success = second as ApiResponse.Success
            assertThat(success.data).isEqualTo(sample)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `usecase forwards error from repository`() = runTest {
        val repoFlow = flow {
            emit(ApiResponse.Loading())
            emit(ApiResponse.Error<List<Holdings>>(errorMessage = "Connect Error", errorCode = 500))
        }

        coEvery { repository.getHoldings() } returns repoFlow

        useCase.getHoldings().test {
            val first = awaitItem()
            assertThat(first).isInstanceOf(ApiResponse.Loading::class.java)

            val second = awaitItem()
            assertThat(second).isInstanceOf(ApiResponse.Error::class.java)
            val error = second as ApiResponse.Error
            assertThat(error.errorMessage).isEqualTo("Connect Error")
            assertThat(error.errorCode).isEqualTo(500)

            cancelAndIgnoreRemainingEvents()
        }
    }
}
