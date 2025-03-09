package com.spendless.widget.presentation.create_transaction

import android.content.Context
import android.content.Intent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.ContentScale
import androidx.glance.layout.Spacer
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.text.FontFamily
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.spendless.widget.apresentation.R
import com.spendless.widget.presentation.ui.CreateTransactionGradient
import com.spendless.widget.presentation.ui.LoginWidgetIcon

class CreateTransactionWidget : GlanceAppWidget() {

    companion object {
        const val INTENT_SOURCE_KEY = "source"
        const val SOURCE = "CreateTransactionWidget"
    }

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            CreateTransactionWidgetContent()
        }
    }
}

@Composable
private fun CreateTransactionWidgetContent() {
    val context = LocalContext.current
    Box(
        modifier = GlanceModifier
            .background(
                imageProvider = ImageProvider(R.drawable.ic_widget_background),
                contentScale = ContentScale.Crop
            )
            .padding(20.dp)
            .clickable(
                actionStartActivity(
                    intent = createMainActivityIntent(context)
                )
            )
    ) {
        Column {
            Image(
                provider = LoginWidgetIcon,
                contentDescription = ""
            )

            Spacer(modifier = GlanceModifier.height(26.dp))

            Text(
                text = "Create Transaction",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = FontFamily("figtree"),
                    color = ColorProvider(MaterialTheme.colorScheme.onPrimary)
                )
            )
        }
    }
}

private fun createMainActivityIntent(context: Context): Intent {
    return Intent().apply {
        setClassName(context.packageName, "com.hrishi.spendless.MainActivity")
        putExtra(CreateTransactionWidget.INTENT_SOURCE_KEY, CreateTransactionWidget.SOURCE)
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
}

@Composable
private fun CreateTransactionWidgetContentWithManualGradient() {
    CreateTransactionGradient(
        modifier = GlanceModifier
            .padding(20.dp)
    ) {
        Image(
            provider = LoginWidgetIcon,
            contentDescription = ""
        )
    }
}

class SpendLessWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = CreateTransactionWidget()
}