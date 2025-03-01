package com.spendless.settings.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hrishi.core.presentation.designsystem.ExitIcon
import com.hrishi.core.presentation.designsystem.LockIcon
import com.hrishi.core.presentation.designsystem.SettingsButton
import com.hrishi.core.presentation.designsystem.SpendLessFinanceTrackerTheme
import com.hrishi.core.presentation.designsystem.components.SpendLessTopBar
import com.hrishi.presentation.ui.ObserveAsEvents
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsHomeScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: SettingsHomeViewModel = koinViewModel(),
    onNavigateToPreference: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onLogout: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            SettingsHomeEvent.Logout -> onLogout()
            SettingsHomeEvent.NavigateToPreferencesScreen -> onNavigateToPreference()
            SettingsHomeEvent.NavigateToSecurityScreen -> onNavigateToSettings()
        }
    }

    SettingsHomeScreen(
        modifier = modifier,
        uiState = uiState,
        onAction = viewModel::onAction
    )
}

@Composable
fun SettingsHomeScreen(
    modifier: Modifier = Modifier,
    uiState: SettingsHomeViewState,
    onAction: (SettingsHomeAction) -> Unit
) {
    Scaffold(
        topBar = {
            SpendLessTopBar(
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .padding(horizontal = 8.dp),
                title = "Settings",
                titleColor = MaterialTheme.colorScheme.onSurface
            )
        },
        containerColor = Color.Transparent
    ) { contentPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
                .padding(contentPadding)
        ) {
            SettingsGroup {
                SettingsItem(
                    icon = SettingsButton,
                    text = "Preferences",
                    onClick = { onAction(SettingsHomeAction.OnPreferencesClick) }
                )

                Spacer(modifier = Modifier.height(8.dp))

                SettingsItem(
                    icon = LockIcon,
                    text = "Security",
                    onClick = { onAction(SettingsHomeAction.OnSecurityClick) }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            SettingsItem(
                icon = ExitIcon,
                text = "Log out",
                iconTint = MaterialTheme.colorScheme.error,
                iconBackgroundColor = MaterialTheme.colorScheme.error.copy(alpha = 0.08f),
                textColor = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.surfaceContainerLowest,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .fillMaxWidth()
                    .padding(4.dp),
                onClick = { onAction(SettingsHomeAction.OnLogoutClick) }
            )
        }
    }
}

@Composable
private fun SettingsGroup(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.surfaceContainerLowest,
                shape = RoundedCornerShape(12.dp)
            )
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        content()
    }
}

@Composable
private fun SettingsItem(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconTint: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    iconBackgroundColor: Color = MaterialTheme.colorScheme.surfaceContainerLow,
    textColor: Color = MaterialTheme.colorScheme.onSurface
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    color = iconBackgroundColor,
                    shape = RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier.size(17.dp),
                tint = iconTint,
                imageVector = icon,
                contentDescription = null
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = text,
            style = MaterialTheme.typography.labelMedium,
            color = textColor
        )
    }
}

@Preview
@Composable
private fun PreviewSettingsHomeScreenRoot() {
    SpendLessFinanceTrackerTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            SettingsHomeScreen(
                uiState = SettingsHomeViewState(),
                onAction = {}
            )
        }
    }
}