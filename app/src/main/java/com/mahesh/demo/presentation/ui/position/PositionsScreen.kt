package com.mahesh.demo.presentation.ui.position

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.mahesh.demo.R

@Composable
fun PositionsScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            stringResource(R.string.position_screen_in_development),
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            color = colorResource(R.color.appPrimary),
            modifier = Modifier.align(Alignment.Center)
        )
    }
}