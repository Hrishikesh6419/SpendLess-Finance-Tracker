package com.spendless.dashboard.presentation.export

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hrishi.core.domain.export.model.ExportType
import com.hrishi.core.presentation.designsystem.CloseIcon
import com.hrishi.core.presentation.designsystem.SpendLessFinanceTrackerTheme
import com.hrishi.core.presentation.designsystem.components.CategorySelector
import com.hrishi.core.presentation.designsystem.components.buttons.SpendLessButton
import com.hrishi.presentation.ui.ObserveAsEvents
import org.koin.androidx.compose.koinViewModel

@Composable
fun ExportTransactionsScreenRoot(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    viewModel: ExportTransactionsViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            ExportTransactionsEvent.CloseBottomSheet -> onDismiss()
            is ExportTransactionsEvent.ExportStatus -> {
                val message = if (event.isExportSuccess) {
                    "Exported successfully to downloads"
                } else {
                    "Export failed"
                }
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    ExportTransactionsScreen(
        modifier = modifier,
        uiState = uiState,
        onAction = viewModel::onAction
    )
}

@Composable
private fun ExportTransactionsScreen(
    modifier: Modifier = Modifier,
    uiState: ExportTransactionsViewState,
    onAction: (ExportTransactionsAction) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {

        Spacer(modifier = Modifier.height(22.dp))

        Row(
            modifier = Modifier
                .height(IntrinsicSize.Max)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Export",
                style = MaterialTheme.typography.titleLarge
            )

            Icon(
                modifier = Modifier.clickable {
                    onAction(ExportTransactionsAction.OnDismissClicked)
                },
                tint = Color.Unspecified,
                imageVector = CloseIcon,
                contentDescription = ""
            )
        }
        Text(
            text = "Export transactions to CSV format",
            style = MaterialTheme.typography.bodySmall
        )

        Spacer(modifier = Modifier.height(24.dp))

        CategorySelector(
            modifier = Modifier.padding(top = 16.dp),
            title = "Export Range",
            selectedOption = uiState.exportType,
            fontStyle = MaterialTheme.typography.labelMedium,
            options = ExportType.entries.toTypedArray(),
            currencyDisplay = { "" },
            currencyTitleDisplay = { it.displayName },
            onItemSelected = {
                onAction(ExportTransactionsAction.OnExportTypeUpdated(it))
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        SpendLessButton(
            buttonText = "Export"
        ) {
            onAction(ExportTransactionsAction.OnExportClicked)
        }
    }
}

@Preview
@Composable
private fun PreviewExportTransactionsScreenRoot() {
    SpendLessFinanceTrackerTheme {
        Surface(
            color = MaterialTheme.colorScheme.surfaceContainerLow
        ) {
            ExportTransactionsScreen(
                uiState = ExportTransactionsViewState(exportType = ExportType.LAST_THREE_MONTH),
                onAction = {

                }
            )
        }
    }
}