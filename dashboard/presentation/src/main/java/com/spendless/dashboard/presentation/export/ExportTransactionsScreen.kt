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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hrishi.core.domain.export.model.ExportType
import com.hrishi.core.presentation.designsystem.CloseIcon
import com.hrishi.core.presentation.designsystem.SpendLessFinanceTrackerTheme
import com.hrishi.core.presentation.designsystem.components.CategorySelector
import com.hrishi.core.presentation.designsystem.components.buttons.SpendLessButton
import com.hrishi.presentation.ui.LocalAuthActionHandler
import com.hrishi.presentation.ui.ObserveAsEvents
import com.hrishi.presentation.ui.UpdateDialogStatusBarAppearance
import com.spendless.dashboard.core.R
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel

@Composable
fun ExportTransactionsScreenRoot(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    viewModel: ExportTransactionsViewModel = koinViewModel()
) {
    val authActionHandler = LocalAuthActionHandler.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    UpdateDialogStatusBarAppearance(isDarkStatusBarIcons = false)

    EventHandler(
        events = viewModel.events,
        onDismiss = onDismiss
    )

    ExportTransactionsScreen(
        modifier = modifier,
        uiState = uiState,
        onAction = { action ->
            when (action) {
                ExportTransactionsAction.OnExportClicked -> {
                    authActionHandler?.invoke { viewModel.onAction(action) }
                }

                else -> viewModel.onAction(action)
            }
        }
    )
}

@Composable
private fun EventHandler(
    events: Flow<ExportTransactionsEvent>,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    ObserveAsEvents(events) { event ->
        when (event) {
            ExportTransactionsEvent.CloseBottomSheet -> onDismiss()
            is ExportTransactionsEvent.ExportStatus -> {
                val message = if (event.isExportSuccess) {
                    context.getString(R.string.exported_successfully_to_downloads)
                } else {
                    context.getString(R.string.export_failed)
                }
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }
    }
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

        HeaderSection(onDismissClicked = { onAction(ExportTransactionsAction.OnDismissClicked) })

        Text(
            text = stringResource(R.string.export_transactions_to_csv_format),
            style = MaterialTheme.typography.bodySmall
        )

        Spacer(modifier = Modifier.height(40.dp))

        ExportTypeSelector(
            selectedOption = uiState.exportType,
            onOptionSelected = { onAction(ExportTransactionsAction.OnExportTypeUpdated(it)) }
        )

        Spacer(modifier = Modifier.height(24.dp))

        SpendLessButton(buttonText = stringResource(R.string.export)) {
            onAction(ExportTransactionsAction.OnExportClicked)
        }
    }
}

@Composable
private fun HeaderSection(
    onDismissClicked: () -> Unit
) {
    Row(
        modifier = Modifier
            .height(IntrinsicSize.Max)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(R.string.export),
            style = MaterialTheme.typography.titleLarge
        )
        Icon(
            modifier = Modifier.clickable { onDismissClicked() },
            tint = Color.Unspecified,
            imageVector = CloseIcon,
            contentDescription = null
        )
    }
}

@Composable
private fun ExportTypeSelector(
    selectedOption: ExportType,
    onOptionSelected: (ExportType) -> Unit
) {
    CategorySelector(
        title = stringResource(R.string.export_range),
        selectedOption = selectedOption,
        fontStyle = MaterialTheme.typography.labelMedium,
        options = ExportType.entries.toTypedArray(),
        currencyDisplay = { "" },
        currencyTitleDisplay = { it.displayName },
        onItemSelected = onOptionSelected
    )
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
                onAction = { }
            )
        }
    }
}