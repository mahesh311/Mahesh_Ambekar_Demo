package com.mahesh.demo.presentation.ui.holdings

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mahesh.demo.R
import com.mahesh.demo.data.utils.ApiResponse
import com.mahesh.demo.presentation.viewmodel.HoldingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchHoldingsScreen(
    holdingsViewModel: HoldingsViewModel = hiltViewModel<HoldingsViewModel>()
) {
    val response = holdingsViewModel.getHoldingsApiResponse.collectAsStateWithLifecycle()
    val actualResponse = response.value

    val allHoldings = (actualResponse as? ApiResponse.Success)?.data ?: emptyList()
    Log.d("SearchHoldingsScreen", "SearchHoldingsScreen: allHoldings = ${allHoldings.size}")

    var query by remember { mutableStateOf("") }
    val filteredList = remember(query, allHoldings) {
        holdingsViewModel.getFilteredData(query, allHoldings)
    }

    Column(Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            placeholder = { Text(stringResource(R.string.search_holdings_hint)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            singleLine = true
        )

        LazyColumn {
            items(filteredList, key = { it.symbol }) { holding ->
                HoldingRow(
                    holding.symbol,
                    holding.quantity,
                    holding.ltp,
                    holding.totalPnl
                )
            }
        }
    }
}
