package com.mahesh.demo.presentation.navigation

sealed class PortfolioNavRoute(val route: String) {
    object PortfolioGraph : PortfolioNavRoute("portfolio_graph")

    object PortfolioHome : PortfolioNavRoute("portfolio_home")
    object SearchHolding : PortfolioNavRoute("search_holding")
}
