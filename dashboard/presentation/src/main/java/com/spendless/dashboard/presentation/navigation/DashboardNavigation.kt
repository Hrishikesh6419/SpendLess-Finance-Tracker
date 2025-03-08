package com.spendless.dashboard.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.hrishi.presentation.ui.AppNavRoute
import com.hrishi.presentation.ui.NavigationRequestHandler
import com.hrishi.presentation.ui.navigation.AllTransactionsScreenRoute
import com.hrishi.presentation.ui.navigation.DashboardBaseRoute
import com.hrishi.presentation.ui.navigation.DashboardScreenRoute
import com.spendless.dashboard.presentation.all_transactions.AllTransactionsScreenRoot
import com.spendless.dashboard.presentation.dashboard.DashboardScreenRoot
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
fun NavGraphBuilder.dashboardNavGraph(
    navController: NavHostController,
    onNavigateToSettings: () -> Unit,
    navigationRequestHandler: NavigationRequestHandler
) {
    navigation<DashboardBaseRoute>(
        startDestination = DashboardScreenRoute
    ) {
        composable<DashboardScreenRoute> {
            DashboardScreenRoot(
                onNavigateToSettings = onNavigateToSettings,
                onNavigateToAllTransactions = {
                    navigationRequestHandler.navigateWithAuthCheck(
                        AppNavRoute(
                            pendingRoute = AllTransactionsScreenRoute
                        )
                    )
                },
                onRequestCreateTransaction = { onAuthSuccessCallback ->
                    navigationRequestHandler.runWithAuthCheck {
                        // This will run after an auth check
                        onAuthSuccessCallback.invoke()
                    }
                }
            )
        }

        composable<AllTransactionsScreenRoute> {
            AllTransactionsScreenRoot(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}