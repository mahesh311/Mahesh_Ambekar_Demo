package com.mahesh.demo.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.mahesh.demo.presentation.ui.holdings.SearchHoldingsScreen
import com.mahesh.demo.presentation.ui.portfolio.PortfolioHomeScreen
import com.mahesh.demo.presentation.ui.topbar.PortfolioTopBar
import com.mahesh.demo.presentation.viewmodel.HoldingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DemoNavGraph() {
    val navController = rememberNavController()
    var onSortClick by remember { mutableStateOf<(() -> Unit)?>(null) }
    var showIcons by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            PortfolioTopBar(
                onSortClick = {
                    onSortClick?.invoke()
                },
                onSearchClick = {
                    navController.navigate(route = PortfolioNavRoute.SearchHolding.route)
                },
                showIcons = showIcons
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            modifier = Modifier
                .padding(innerPadding)
                .background(Color.White),
            startDestination = PortfolioNavRoute.PortfolioGraph.route,
            route = "main_graph"
        ) {
            navigation(
                startDestination = PortfolioNavRoute.PortfolioHome.route,
                route = PortfolioNavRoute.PortfolioGraph.route
            ) {
                composable(PortfolioNavRoute.PortfolioHome.route) { backStackEntry ->
                    val parentEntry = remember(backStackEntry) {
                        navController.getBackStackEntry(PortfolioNavRoute.PortfolioGraph.route)
                    }
                    val holdingsViewModel: HoldingsViewModel = hiltViewModel(parentEntry)
                    showIcons = true
                    PortfolioHomeScreen(setSortHandler = {
                        onSortClick = it
                    }, holdingsViewModel)
                }

                composable(PortfolioNavRoute.SearchHolding.route) { backStackEntry ->
                    val parentEntry = remember(backStackEntry) {
                        navController.getBackStackEntry(PortfolioNavRoute.PortfolioGraph.route)
                    }
                    val holdingsViewModel: HoldingsViewModel = hiltViewModel(parentEntry)
                    showIcons = false
                    SearchHoldingsScreen(holdingsViewModel)
                }
            }
        }
    }
}