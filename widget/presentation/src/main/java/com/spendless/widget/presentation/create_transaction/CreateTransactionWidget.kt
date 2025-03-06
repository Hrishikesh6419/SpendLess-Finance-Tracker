package com.spendless.widget.presentation.create_transaction

import android.content.Context
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
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

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            CreateTransactionWidgetContent()
        }
    }
}

@Composable
private fun CreateTransactionWidgetContent() {
    Box(
        modifier = GlanceModifier
            .background(
                imageProvider = ImageProvider(R.drawable.ic_widget_background),
                contentScale = ContentScale.Crop
            )
            .padding(20.dp)
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