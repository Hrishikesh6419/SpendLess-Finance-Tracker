package com.hrishi.spendless

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.hrishi.core.presentation.designsystem.SpendLessFinanceTrackerTheme
import com.hrishi.spendless.navigation.NavigationRoot
import com.spendless.widget.presentation.create_transaction.CreateTransactionWidget

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val source = intent?.getStringExtra(CreateTransactionWidget.INTENT_SOURCE_KEY)
        if (source == CreateTransactionWidget.SOURCE) {
            Log.d("hrishiii", "onCreate: Clicked from Create Transaction widget")
        } else {
            Log.d("hrishiii", "onCreate: Source is null or not from widget")
        }

        setContent {
            SpendLessFinanceTrackerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavigationRoot(navController = navController)
                }
            }
        }
    }
}