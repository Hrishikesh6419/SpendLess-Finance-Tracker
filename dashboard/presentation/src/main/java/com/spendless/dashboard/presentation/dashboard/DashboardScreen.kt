package com.spendless.dashboard.presentation.dashboard

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
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
import com.hrishi.core.domain.formatting.NumberFormatter
import com.hrishi.core.domain.model.Currency
import com.hrishi.core.domain.model.DecimalSeparator
import com.hrishi.core.domain.model.ExpenseFormat
import com.hrishi.core.domain.model.ThousandsSeparator
import com.hrishi.core.presentation.designsystem.DownloadButton
import com.hrishi.core.presentation.designsystem.SettingsButton
import com.hrishi.core.presentation.designsystem.SpendLessFinanceTrackerTheme
import com.hrishi.core.presentation.designsystem.components.LargestTransactionView
import com.hrishi.core.presentation.designsystem.components.PopularCategoryView
import com.hrishi.core.presentation.designsystem.components.PreviousWeekTotalView
import com.hrishi.core.presentation.designsystem.components.SpendLessScaffold
import com.hrishi.core.presentation.designsystem.components.SpendLessTopBar
import com.hrishi.core.presentation.designsystem.components.TransactionItemView
import com.hrishi.core.presentation.designsystem.components.buttons.SpendLessFloatingActionButton
import com.hrishi.core.presentation.designsystem.model.TransactionCategoryTypeUI
import com.hrishi.presentation.ui.ObserveAsEvents
import com.spendless.dashboard.presentation.create_screen.CreateTransactionScreenRoot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.Month

@OptIn(ExperimentalMaterial3Api::class, ExperimentalCoroutinesApi::class)
@Composable
fun DashboardScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: DashboardViewModel = koinViewModel(),
    onNavigateToSettings: () -> Unit
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

            DashboardEvent.NavigateToSettings -> onNavigateToSettings()
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier,
    uiState: DashboardViewState,
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
            properties = ModalBottomSheetProperties(
                shouldDismissOnBackPress = false
            ),
            dragHandle = null,
            modifier = Modifier
                .fillMaxHeight()
                .windowInsetsPadding(WindowInsets.statusBars)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                CreateTransactionScreenRoot(
                    onDismiss = {
                        onAction(DashboardAction.UpdatedBottomSheet(false))
                    }
                )
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
                onEndIcon2Click = {
                    onAction(DashboardAction.OnSettingsClicked)
                },
                endIcon1Color = MaterialTheme.colorScheme.onPrimary,
                endIcon1BackgroundColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.18f),
                endIcon2Color = MaterialTheme.colorScheme.onPrimary,
                endIcon2BackgroundColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.18f),
                title = uiState.username,
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
                        text = uiState.accountBalance,
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
                    uiState.mostPopularCategory?.let { popularCategory ->
                        PopularCategoryView(
                            modifier = Modifier
                                .padding(horizontal = 16.dp),
                            icon = popularCategory.symbol,
                            title = popularCategory.title,
                            description = "Most popular category"
                        )
                    }
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
                            isEmptyTransactions = uiState.largestTransaction == null,
                            title = uiState.largestTransaction?.name.orEmpty(),
                            description = "Largest transaction",
                            amount = uiState.largestTransaction?.amount ?: "",
                            date = uiState.largestTransaction?.date.orEmpty()
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        PreviousWeekTotalView(
                            modifier = Modifier.fillMaxHeight(),
                            amount = uiState.previousWeekTotal,
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

                        Spacer(modifier = Modifier.height(16.dp))

                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            uiState.transactions?.forEach { transactionGroup ->
                                stickyHeader {
                                    Text(
                                        text = transactionGroup.dateLabel,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = MaterialTheme.colorScheme.onSurface.copy(
                                                alpha = 0.70f
                                            )
                                        ),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(MaterialTheme.colorScheme.background)
                                            .padding(vertical = 8.dp)
                                            .padding(horizontal = 4.dp)
                                    )
                                }

                                items(transactionGroup.transactions) { transaction ->
                                    TransactionItemView(
                                        icon = transaction.transactionCategory.symbol,
                                        title = transaction.title,
                                        category = transaction.transactionCategory.title,
                                        amount = transaction.amount,
                                        note = transaction.note,
                                        displayAmount = { amount ->
                                            NumberFormatter.formatAmount(
                                                amount = amount,
                                                expenseFormat = uiState.preference?.expenseFormat
                                                    ?: ExpenseFormat.MINUS_PREFIX,
                                                decimalSeparator = uiState.preference?.decimalSeparator
                                                    ?: DecimalSeparator.DOT,
                                                thousandsSeparator = uiState.preference?.thousandsSeparator
                                                    ?: ThousandsSeparator.COMMA,
                                                currency = uiState.preference?.currency
                                                    ?: Currency.USD
                                            )
                                        },
                                        onCardClicked = {}
                                    )
                                }

                                item {
                                    Spacer(modifier = Modifier.height(12.dp))
                                }
                            }
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
                uiState = DashboardViewState(
                    username = "Hrishi",
                    accountBalance = "\$10,382.45",
                    mostPopularCategory = TransactionCategoryTypeUI.TRANSPORTATION,
                    previousWeekTotal = "\$100.45",
                    largestTransaction = LargestTransaction(
                        name = "Adobe Yearly",
                        amount = "-\$59.99",
                        date = "Jan 7, 2025"
                    ),
                    transactions = listOf(
                        TransactionGroupUIItem(
                            dateLabel = "TODAY",
                            transactions = listOf(
                                TransactionUIItem(
                                    transactionCategory = TransactionCategoryTypeUI.OTHER,
                                    title = "Amazon",
                                    note = "Hi",
                                    amount = BigDecimal.TEN.negate(),
                                    date = LocalDateTime.of(
                                        2025,
                                        Month.MARCH,
                                        30,
                                        0,
                                        0
                                    )
                                )
                            )
                        ),
                        TransactionGroupUIItem(
                            dateLabel = "JANUARY 9",
                            transactions = listOf(
                                TransactionUIItem(
                                    transactionCategory = TransactionCategoryTypeUI.OTHER,
                                    title = "Amazon",
                                    note = "Hi",
                                    amount = BigDecimal.TEN.negate(),
                                    date = LocalDateTime.of(
                                        2025,
                                        Month.MARCH,
                                        30,
                                        0,
                                        0
                                    )
                                )
                            )
                        )
                    )
                ),
                snackBarHostState = SnackbarHostState(),
                scope = rememberCoroutineScope(),
                onAction = {

                },
                bottomSheetState = rememberModalBottomSheetState(),
            )
        }
    }
}

