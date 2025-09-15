package com.mahesh.demo.presentation.ui.holdings

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mahesh.demo.R
import com.mahesh.demo.data.api.NetworkConstant
import com.mahesh.demo.data.utils.ApiResponse
import com.mahesh.demo.presentation.entities.SortType
import com.mahesh.demo.presentation.viewmodel.HoldingsViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HoldingsHomeScreen(
    setSortHandler: (() -> Unit) -> Unit,
    holdingsViewModel: HoldingsViewModel = hiltViewModel<HoldingsViewModel>()
) {
    val tag = "HoldingsHomeScreen"
    var expandedSummary by remember { mutableStateOf(false) }

    var isLoading by remember { mutableStateOf(false) }
    val pullToRefreshState = rememberPullToRefreshState()

    val response = holdingsViewModel.getHoldingsApiResponse.collectAsStateWithLifecycle()
    val actualResponse = response.value

    var showSortSheet by remember { mutableStateOf(false) }
    var sortType by remember { mutableStateOf(SortType.NAME_ASC) }

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        Log.d(tag, "HoldingsHomeScreen: LaunchedEffect IN")
        if (actualResponse !is ApiResponse.Success){
            isLoading = true
            holdingsViewModel.getHoldings()
        }
    }

    LaunchedEffect(sortType) {
        coroutineScope.launch {
            listState.animateScrollToItem(0)
        }
    }

    setSortHandler {
        Log.d(tag, "HoldingsHomeScreen: showSortSheet")
        showSortSheet = true
    }

    PullToRefreshBox(
        isRefreshing = isLoading,
        onRefresh = {
            Log.d(tag, "HoldingsHomeScreen: PullToRefreshBox onRefresh")
            if (!isLoading) {
                isLoading = true
                holdingsViewModel.getHoldings()
            }
        },
        modifier = Modifier.fillMaxSize(), state = pullToRefreshState,
        content = {
            when (actualResponse) {
                is ApiResponse.Success -> {
                    Log.d(tag, "HoldingsHomeScreen: Success = ${actualResponse.data}")
                    isLoading = false

                    val sortedHoldings = try {
                        when (sortType) {
                            SortType.NAME_ASC -> actualResponse.data.sortedBy { it.symbol }
                            SortType.NAME_DESC -> actualResponse.data.sortedByDescending { it.symbol }
                            SortType.LTP_ASC -> actualResponse.data.sortedBy { it.ltp }
                            SortType.LTP_DESC -> actualResponse.data.sortedByDescending { it.ltp }
                            SortType.TOTAL_PL_ASC -> actualResponse.data.sortedBy { it.totalPnl }
                            SortType.TOTAL_PL_DESC -> actualResponse.data.sortedByDescending { it.totalPnl }
                        }
                    } catch (e: Exception) {
                        Log.e(
                            tag,
                            "HoldingsHomeScreen: Exception in sorting, assigning original data",
                            e
                        )
                        actualResponse.data
                    }

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 5.dp, start = 5.dp, end = 5.dp, bottom = 55.dp),
                        state = listState,
                    ) {
                        items(sortedHoldings, key = { it.symbol }) { holding ->
                            HoldingRow(
                                holding.symbol,
                                holding.quantity,
                                holding.ltp,
                                holding.totalPnl
                            )
                        }
                    }

                    HoldingSummaryCard(
                        currentValue = holdingsViewModel.totalCurrentValue,
                        totalInvestment = holdingsViewModel.totalInvestment,
                        todaysPnl = holdingsViewModel.todaysPnl,
                        totalPnl = holdingsViewModel.totalPnl,
                        totalPnlPercent = holdingsViewModel.pnlPercent,
                        expanded = expandedSummary,
                        onToggle = { expandedSummary = !expandedSummary },
                        modifier = Modifier.align(Alignment.BottomCenter)
                    )

                    if (showSortSheet) {
                        SortBottomSheet(
                            onDismiss = { showSortSheet = false },
                            onSortSelected = { selected ->
                                sortType = selected
                            }
                        )
                    }
                }

                is ApiResponse.Error -> {
                    Log.d(tag, "HoldingsHomeScreen: Error = ${actualResponse.errorMessage}")
                    isLoading = false
                    Row(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .clickable(true, onClick = {
                                isLoading = true
                                holdingsViewModel.getHoldings()
                            })
                    ) {
                        IconButton(onClick = {
                            isLoading = true
                            holdingsViewModel.getHoldings()
                        }, modifier = Modifier.align(Alignment.CenterVertically)) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = stringResource(R.string.retry_desc)
                            )
                        }
                        Text(
                            text = actualResponse.errorMessage
                                ?: NetworkConstant.SOMETHING_WENT_WRONG,
                        )
                    }
                }

                is ApiResponse.Loading -> {
                    Log.d(tag, "HoldingsHomeScreen: Loading")
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White)
                    ) {
                        Row(modifier = Modifier.align(Alignment.Center)) {
                            Text(
                                text = stringResource(R.string.please_wait_msg),
                                modifier = Modifier.align(Alignment.CenterVertically)
                            )
                        }
                    }
                }
            }
        })
}