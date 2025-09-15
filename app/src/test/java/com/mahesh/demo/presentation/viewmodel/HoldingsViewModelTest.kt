package com.mahesh.demo.presentation.viewmodel

import android.util.Log
import com.google.common.truth.Truth.assertThat
import com.mahesh.demo.data.utils.ApiResponse
import com.mahesh.demo.domain.usecases.HoldingsUseCase
import com.mahesh.demo.presentation.entities.Holdings
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HoldingsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val useCase: HoldingsUseCase = mockk()
    private lateinit var viewModel: HoldingsViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        every { Log.e(any(), any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0
        viewModel = HoldingsViewModel(useCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getHoldings emits Success after Loading`() = runTest {
        val sample = listOf(
            Holdings("ABC", 1, 100.0, 90.0, 95.0, 0.0, 0.0)
        )

        coEvery { useCase.getHoldings() } returns flow {
            emit(ApiResponse.Success(sample))
        }

        viewModel.getHoldings()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.getHoldingsApiResponse.value
        assertThat(state).isInstanceOf(ApiResponse.Success::class.java)
        val success = state as ApiResponse.Success
        assertThat(success.data).isEqualTo(sample)
    }

    @Test
    fun `getFilteredData returns all if query is blank`() {
        val holdings = listOf(
            Holdings(
                symbol = "ABC",
                quantity = 10,
                ltp = 100.0,
                avgPrice = 90.0,
                close = 95.0,
                totalPnl = 50.0,
                todayPnl = 50.0
            )
        )
        val result = viewModel.getFilteredData("", holdings)
        assertThat(result).isEqualTo(holdings)
    }

    @Test
    fun `getFilteredData returns filtered list when query matches`() {
        val holdings = listOf(
            Holdings(
                symbol = "ABC",
                quantity = 10,
                ltp = 100.0,
                avgPrice = 90.0,
                close = 95.0,
                totalPnl = 50.0,
                todayPnl = 50.0
            ),
            Holdings(
                symbol = "XYZ",
                quantity = 5,
                ltp = 200.0,
                avgPrice = 150.0,
                close = 210.0,
                totalPnl = 300.0,
                todayPnl = 300.0
            )
        )
        val result = viewModel.getFilteredData("ABC", holdings)
        assertThat(result).containsExactly(holdings[0])
    }

    @Test
    fun `getFilteredData returns empty list when query does not match`() {
        val holdings = listOf(
            Holdings(
                symbol = "ABC",
                quantity = 10,
                ltp = 100.0,
                avgPrice = 90.0,
                close = 95.0,
                totalPnl = 50.0,
                todayPnl = 50.0
            )
        )
        val result = viewModel.getFilteredData("ZZZ", holdings)
        assertThat(result).isEmpty()
    }
}
