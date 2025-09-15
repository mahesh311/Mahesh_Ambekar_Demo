package com.mahesh.demo.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahesh.demo.data.utils.ApiResponse
import com.mahesh.demo.domain.usecases.HoldingsUseCase
import com.mahesh.demo.presentation.entities.Holdings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HoldingsViewModel @Inject constructor(val holdingsUseCase: HoldingsUseCase) :
    ViewModel() {

    val tag: String = HoldingsViewModel::class.java.simpleName
    private val _getHoldingsApiResponse =
        MutableStateFlow<ApiResponse<List<Holdings>>>(ApiResponse.Loading())
    val getHoldingsApiResponse: StateFlow<ApiResponse<List<Holdings>>> = _getHoldingsApiResponse

    var totalCurrentValue: Double = 0.0
    var totalInvestment: Double = 0.0
    var totalPnl: Double = 0.0
    var todaysPnl: Double = 0.0
    var pnlPercent: Double = 0.0

    fun getHoldings() {
        viewModelScope.launch {
            try {
                Log.d(tag, "getHoldings: calling getHolding API")
                holdingsUseCase.getHoldings().collect { holdingDetail ->
                    try {
                        _getHoldingsApiResponse.value = holdingDetail
                        if (holdingDetail is ApiResponse.Success<List<Holdings>>) {
                            totalCurrentValue =
                                (_getHoldingsApiResponse.value as ApiResponse.Success<List<Holdings>>).data.sumOf { it.ltp * it.quantity }
                            totalInvestment =
                                (_getHoldingsApiResponse.value as ApiResponse.Success<List<Holdings>>).data.sumOf { it.avgPrice * it.quantity }
                            totalPnl = totalCurrentValue - totalInvestment
                            todaysPnl =
                                (_getHoldingsApiResponse.value as ApiResponse.Success<List<Holdings>>).data.sumOf { (it.close - it.ltp) * it.quantity }
                            pnlPercent = try {
                                (totalPnl / totalInvestment) * 100
                            } catch (e: ArithmeticException) {
                                Log.e(
                                    tag,
                                    "getHoldings: Exception in calculating pnlPercent",
                                    e
                                )
                                0.0
                            }
                        }
                    } catch (e: Exception) {
                        Log.e(tag, "getHoldings: Exception in processing holding data", e)
                    }
                }
            } catch (e: Exception) {
                Log.e(tag, "getHoldings: Exception in getting holdings data", e)
            }
        }
    }

    fun getFilteredData(query: String, allHoldings: List<Holdings>) =
        if (query.isBlank()) allHoldings
        else allHoldings.filter { it.symbol.contains(query, ignoreCase = true) }
}