package com.spendless.dashboard.presentation.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hrishi.core.presentation.designsystem.DownloadButton
import com.hrishi.core.presentation.designsystem.SettingsButton
import com.hrishi.core.presentation.designsystem.SpendLessFinanceTrackerTheme
import com.hrishi.core.presentation.designsystem.components.LargestTransactionView
import com.hrishi.core.presentation.designsystem.components.PopularCategoryView
import com.hrishi.core.presentation.designsystem.components.PreviousWeekTotalView
import com.hrishi.core.presentation.designsystem.components.SpendLessScaffold
import com.hrishi.core.presentation.designsystem.components.SpendLessTopBar
import com.hrishi.core.presentation.designsystem.components.buttons.SpendLessFloatingActionButton
import com.hrishi.presentation.ui.ObserveAsEvents
import com.spendless.dashboard.presentation.create_screen.CreateTransactionScreenRoot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: DashboardViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val snackBarHostState = remember { SnackbarHostState() }

    ObserveAsEvents(viewModel.events) {
        when (it) {

            DashboardEvent.NavigateTest -> {

            }
        }
    }

    DashboardScreen(
        modifier = modifier,
        uiState = uiState,
        snackBarHostState = snackBarHostState,
        bottomSheetState = bottomSheetState,
        scope = scope,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier,
    uiState: DashboardState,
    snackBarHostState: SnackbarHostState,
    bottomSheetState: SheetState,
    scope: CoroutineScope,
    onAction: (DashboardAction) -> Unit,
) {
    if (uiState.showCreateTransactionSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                onAction(DashboardAction.UpdatedBottomSheet(false))
            },
            sheetState = bottomSheetState,
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            modifier = Modifier
                .fillMaxHeight()
                .windowInsetsPadding(WindowInsets.statusBars)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                CreateTransactionScreenRoot()
            }
        }
    }

    SpendLessScaffold(
        topAppBar = {
            SpendLessTopBar(
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .padding(horizontal = 8.dp),
                startIcon = null,
                endIcon1 = DownloadButton,
                endIcon2 = SettingsButton,
                endIcon1Color = MaterialTheme.colorScheme.onPrimary,
                endIcon1BackgroundColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.18f),
                endIcon2Color = MaterialTheme.colorScheme.onPrimary,
                endIcon2BackgroundColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.18f),
                title = "rockefeller74",
            )
        },
        floatingActionButton = {
            SpendLessFloatingActionButton(
                onClick = {
                    scope.launch {
                        onAction(DashboardAction.UpdatedBottomSheet(true))
                    }
                }
            )
        },
    ) { contentPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(top = contentPadding.calculateTopPadding())
        ) {
            Column {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    Spacer(modifier = Modifier.height(36.dp))
                    Text(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        text = "\$10,382.45",
                        style = MaterialTheme.typography.displayLarge.copy(
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                    Text(
                        modifier = Modifier
                            .padding(top = 2.dp)
                            .align(Alignment.CenterHorizontally),
                        text = "Account Balance",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onPrimary.copy(
                                alpha = 0.80f
                            )
                        )
                    )
                    Spacer(modifier = Modifier.height(46.dp))
                    PopularCategoryView(
                        modifier = Modifier
                            .padding(horizontal = 16.dp),
                        icon = "\uD83C\uDF55",
                        title = "Food & Groceries",
                        description = "Most popular category"
                    )
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .padding(top = 8.dp)
                            .height(IntrinsicSize.Max)
                    ) {
                        LargestTransactionView(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                            title = "Adobe",
                            description = "Largest transaction",
                            amount = "-\$59.99",
                            date = "Jan 7, 2025",
                        )
                        Spacer(modifier = Modifier.width(8.dp))

                        PreviousWeekTotalView(
                            modifier = Modifier.fillMaxHeight(),
                            amount = "-\$762.20",
                            description = "Previous week"
                        )


                    }
                }

                Spacer(Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .background(
                            color = MaterialTheme.colorScheme.background,
                            shape = RoundedCornerShape(
                                topStart = 16.dp,
                                topEnd = 16.dp
                            )
                        )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp, vertical = 16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Latest Transactions",
                                style = MaterialTheme.typography.titleLarge
                            )
                            Text(
                                text = "Show all",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = MaterialTheme.colorScheme.primary
                                )
                            )
                        }
                    }
                }

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun PreviewDashboardScreen() {
    SpendLessFinanceTrackerTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            DashboardScreen(
                modifier = Modifier,
                uiState = DashboardState(),
                snackBarHostState = SnackbarHostState(),
                scope = rememberCoroutineScope(),
                onAction = {

                },
                bottomSheetState = rememberModalBottomSheetState(),
            )
        }
    }
}

