package com.mahesh.demo.presentation.ui.topbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.mahesh.demo.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PortfolioTopBar(
    modifier: Modifier = Modifier,
    onSortClick: () -> Unit,
    onSearchClick: () -> Unit,
    showIcons: Boolean = true
) {
    Column(modifier = modifier.fillMaxWidth()) {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = colorResource(R.color.appPrimary),
                titleContentColor = Color.White,
                navigationIconContentColor = Color.White,
                actionIconContentColor = Color.White
            ),
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                            .background(colorResource(R.color.profile_bg)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person, contentDescription = stringResource(
                                R.string.profile_desc
                            )
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        stringResource(R.string.portfolio_title),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            },
            actions = {
                if (showIcons) {
                    IconButton(onClick = {
                        onSortClick()
                    }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.outline_sort_24),
                            contentDescription = stringResource(
                                R.string.sort_desc
                            )
                        )
                    }

                    IconButton(onClick = { onSearchClick() }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = stringResource(R.string.search_desc)
                        )
                    }
                }
            }
        )
    }
}
