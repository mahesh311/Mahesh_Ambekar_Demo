package com.mahesh.demo.presentation.ui.portfolio

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.mahesh.demo.R
import com.mahesh.demo.presentation.ui.holdings.HoldingsHomeScreen
import com.mahesh.demo.presentation.ui.position.PositionsScreen
import com.mahesh.demo.presentation.viewmodel.HoldingsViewModel
import kotlinx.coroutines.launch

@Composable
fun PortfolioHomeScreen(
    setSortHandler: (() -> Unit) -> Unit,
    holdingsViewModel: HoldingsViewModel = hiltViewModel<HoldingsViewModel>()
) {
    val pagerState =
        rememberPagerState(1, pageCount = { 2 })
    val scope = rememberCoroutineScope()
    Column {
        TabRow(
            selectedTabIndex = 1,
            containerColor = Color.White,
            contentColor = Color.White,
            modifier = Modifier,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                    color = colorResource(R.color.appPrimary)
                )
            }
        ) {
            Tab(
                selected = pagerState.currentPage == 0,
                onClick = {
                    Log.d("PortfolioHomeScreen", "PortfolioHomeScreen currentPage : 0")
                    scope.launch {
                        pagerState.animateScrollToPage(0)
                    }
                },
                selectedContentColor = colorResource(R.color.dark_grey),
                unselectedContentColor = colorResource(R.color.light_grey)
            ) {
                Text(
                    text = stringResource(R.string.positions_title),
                    modifier = Modifier.padding(vertical = 12.dp, horizontal = 12.dp),
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                )
            }

            Tab(
                selected = pagerState.currentPage == 1,
                onClick = {
                    Log.d("PortfolioHomeScreen", "PortfolioHomeScreen currentPage: 1")
                    scope.launch {
                        pagerState.animateScrollToPage(1)
                    }
                },
                selectedContentColor = colorResource(R.color.dark_grey),
                unselectedContentColor = colorResource(R.color.light_grey)
            ) {
                Text(
                    stringResource(R.string.holdings_title),
                    modifier = Modifier.padding(vertical = 12.dp, horizontal = 12.dp),
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            when (page) {
                0 -> {
                    PositionsScreen()
                }

                1 -> {
                    HoldingsHomeScreen(setSortHandler, holdingsViewModel)
                }
            }
        }
    }
}