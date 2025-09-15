package com.mahesh.demo.presentation.ui.holdings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mahesh.demo.R
import java.text.NumberFormat
import java.util.Locale

@Composable
fun HoldingRow(
    symbol: String,
    netQty: Int,
    ltp: Double,
    pnl: Double,
    modifier: Modifier = Modifier
) {
    val locale = Locale.Builder().setLanguage("en").setRegion("IN").build()
    val currencyFormat = NumberFormat.getCurrencyInstance(locale)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = symbol,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "LTP: ${currencyFormat.format(ltp)}",
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Net Qty: $netQty",
                style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
            )
            Text(
                text = "P&L: ${currencyFormat.format(pnl)}",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = if (pnl >= 0) colorResource(R.color.profit_indicator) else colorResource(
                        R.color.loss_indicator
                    ),
                    fontWeight = FontWeight.SemiBold
                )
            )
        }

        HorizontalDivider(
            modifier = Modifier.padding(top = 8.dp),
            thickness = 1.dp,
            color = Color.LightGray.copy(alpha = 0.6f)
        )
    }
}
