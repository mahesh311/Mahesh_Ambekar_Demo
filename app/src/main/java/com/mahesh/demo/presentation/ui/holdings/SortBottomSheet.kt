package com.mahesh.demo.presentation.ui.holdings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mahesh.demo.R
import com.mahesh.demo.presentation.entities.SortType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortBottomSheet(
    onDismiss: () -> Unit,
    onSortSelected: (SortType) -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        containerColor = Color.White
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(stringResource(R.string.sort_by_title), style = MaterialTheme.typography.titleMedium)

            Spacer(Modifier.height(12.dp))

            Text(
                stringResource(R.string.name_asc),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onSortSelected(SortType.NAME_ASC)
                        onDismiss()
                    }
                    .padding(vertical = 12.dp)
            )

            Text(
                stringResource(R.string.name_desc),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onSortSelected(SortType.NAME_DESC)
                        onDismiss()
                    }
                    .padding(vertical = 12.dp)
            )

            Text(
                stringResource(R.string.ltp_asc),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onSortSelected(SortType.LTP_ASC)
                        onDismiss()
                    }
                    .padding(vertical = 12.dp)
            )

            Text(
                stringResource(R.string.ltp_desc),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onSortSelected(SortType.LTP_DESC)
                        onDismiss()
                    }
                    .padding(vertical = 12.dp)
            )

            Text(
                stringResource(R.string.total_pl_asc),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onSortSelected(SortType.TOTAL_PL_ASC)
                        onDismiss()
                    }
                    .padding(vertical = 12.dp)
            )

            Text(
                stringResource(R.string.total_pl_desc),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onSortSelected(SortType.TOTAL_PL_DESC)
                        onDismiss()
                    }
                    .padding(vertical = 12.dp)
            )
        }
    }
}
