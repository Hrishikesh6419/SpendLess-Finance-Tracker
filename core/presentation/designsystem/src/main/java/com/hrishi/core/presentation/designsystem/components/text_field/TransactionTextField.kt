package com.hrishi.core.presentation.designsystem.components.text_field

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hrishi.core.presentation.designsystem.SpendLessSuccess
import java.math.BigDecimal
import java.math.RoundingMode

enum class ExpenseFormat {
    MINUS_SIGN,
    PARENTHESES
}

enum class ThousandsSeparator(val char: Char) {
    DOT('.'),
    COMMA(','),
    SPACE(' ')
}

enum class DecimalSeparator(val char: Char) {
    DOT('.'),
    COMMA(',')
}

@Composable
fun TransactionTextField(
    value: BigDecimal,
    onValueChange: (BigDecimal) -> Unit,
    modifier: Modifier = Modifier,
    isExpense: Boolean = false,
    expenseFormat: ExpenseFormat = ExpenseFormat.MINUS_SIGN,
    currencySymbol: String = "$",
    decimalSeparator: DecimalSeparator = DecimalSeparator.DOT,
    thousandSeparator: ThousandsSeparator = ThousandsSeparator.COMMA,
    expenseColor: Color = MaterialTheme.colorScheme.error,
    incomeColor: Color = SpendLessSuccess,
    emptyAmountColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
    enteredAmountColor: Color = MaterialTheme.colorScheme.onSurface,
    textStyle: TextStyle = MaterialTheme.typography.displayMedium,
) {
    val formattedValue = try {
        // Format to 2 decimal places, take absolute value, and remove decimal point
        value.abs().setScale(2, RoundingMode.HALF_UP).toString().replace(".", "")
    } catch (e: Exception) {
        "0"
    }

    var textFieldValueState by remember(value) {
        mutableStateOf(
            TextFieldValue(
                text = formattedValue,
                selection = TextRange(formattedValue.length)
            )
        )
    }

    val signColor = if (isExpense) expenseColor else incomeColor

    BasicTextField(
        modifier = modifier.width(IntrinsicSize.Min),
        value = textFieldValueState,
        onValueChange = { newValue ->
            try {
                val filteredText = newValue.text.filter { it.isDigit() }
                // Handle empty input by defaulting to zero
                val finalText = filteredText.ifEmpty { "0" }

                textFieldValueState = TextFieldValue(
                    text = finalText,
                    selection = TextRange(finalText.length)
                )

                // Convert to BigDecimal with proper decimal placement
                // If length <= 2, then it's a decimal amount (e.g., ".42")
                val intPart = if (finalText.length <= 2) "0" else finalText.dropLast(2)
                // Last two digits always represent decimal places, pad if needed
                val decPart = finalText.takeLast(2).padStart(2, '0')
                val numericString = "$intPart.$decPart"

                val newBigDecimal = BigDecimal(numericString)
                onValueChange(newBigDecimal)
            } catch (e: Exception) {
                // no-op
            }
        },
        textStyle = textStyle,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        visualTransformation = remember(
            isExpense, expenseFormat, currencySymbol, decimalSeparator,
            thousandSeparator, signColor, emptyAmountColor,
            enteredAmountColor, textStyle
        ) {
            object : VisualTransformation {
                override fun filter(text: AnnotatedString): TransformedText {
                    val (formattedNumber, isZeroAmount) = formatAmount(
                        text.text,
                        decimalSeparator.char,
                        thousandSeparator.char
                    )

                    var cursorPosition = 0

                    val formattedText = buildAnnotatedString {
                        fun spanStyle(color: Color) = SpanStyle(
                            fontFamily = textStyle.fontFamily,
                            fontSize = textStyle.fontSize,
                            fontWeight = textStyle.fontWeight,
                            color = color
                        )

                        val amountColor = if (isZeroAmount) emptyAmountColor else enteredAmountColor

                        when {
                            // Negative with parentheses: ($100.00)
                            isExpense && expenseFormat == ExpenseFormat.PARENTHESES -> {
                                withStyle(spanStyle(signColor)) {
                                    append("("); append(currencySymbol)
                                }
                                withStyle(spanStyle(amountColor)) { append(formattedNumber) }
                                cursorPosition = length
                                withStyle(spanStyle(signColor)) { append(")") }
                            }
                            // Negative with minus sign: -$100.00
                            isExpense && expenseFormat == ExpenseFormat.MINUS_SIGN -> {
                                withStyle(spanStyle(signColor)) {
                                    append("-"); append(currencySymbol)
                                }
                                withStyle(spanStyle(amountColor)) { append(formattedNumber) }
                                cursorPosition = length
                            }
                            // Positive: $100.00
                            else -> {
                                withStyle(spanStyle(signColor)) { append(currencySymbol) }
                                withStyle(spanStyle(amountColor)) { append(formattedNumber) }
                                cursorPosition = length
                            }
                        }
                    }

                    // Create mapping between original and transformed text for cursor positioning
                    return TransformedText(formattedText, object : OffsetMapping {
                        override fun originalToTransformed(offset: Int) = cursorPosition
                        override fun transformedToOriginal(offset: Int) = text.length
                    })
                }

                private fun formatAmount(
                    text: String,
                    decSep: Char,
                    thousandSep: Char
                ): Pair<String, Boolean> {
                    // Handle empty input - display as "00.00"
                    if (text.isEmpty()) return Pair("00$decSep" + "00", true)

                    // Ensure we have at least 3 digits (1 for integer, 2 for decimal)
                    // by padding with leading zeros
                    val paddedValue = text.padStart(3, '0')

                    // Last two digits are always decimal places
                    val decimalValue = paddedValue.takeLast(2)

                    // Everything else is the integer part
                    val integerValue = paddedValue.dropLast(2)

                    // Check if the entire amount is zero (allows for "0.00", "00.00", etc.)
                    val isZeroAmount = integerValue.trimStart('0').isEmpty() && decimalValue == "00"

                    val formattedInt = when {
                        // Empty or zero integer part - display as "00"
                        integerValue.isEmpty() || integerValue == "0" ||
                                integerValue.trimStart('0').isEmpty() -> "00"

                        // Single digit integer part - pad with leading zero (e.g., "01", "09")
                        integerValue.trimStart('0').length == 1 ->
                            "0${integerValue.trimStart('0')}"

                        // Multi-digit integer part - add thousand separators
                        else -> integerValue.trimStart('0')  // Remove leading zeros
                            .reversed()                       // Reverse to process from right to left
                            .chunked(3)                       // Group into chunks of 3 digits
                            .joinToString(thousandSep.toString()) // Join with thousand separator
                            .reversed()                       // Reverse back to normal order
                    }

                    return Pair("$formattedInt$decSep$decimalValue", isZeroAmount)
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun CurrencyTextFieldPreview() {
    MaterialTheme {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Default with MINUS_SIGN:")
            TransactionTextField(
                value = BigDecimal("-100000.00"),
                onValueChange = {
                    Log.d("Preview", "Value changed: $it")
                },
                isExpense = true,
                expenseFormat = ExpenseFormat.MINUS_SIGN,
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("MINUS_SIGN with SPACE separator:")
            TransactionTextField(
                value = BigDecimal("-100000.00"),
                onValueChange = {
                    Log.d("Preview", "Value changed: $it")
                },
                isExpense = true,
                expenseFormat = ExpenseFormat.MINUS_SIGN,
                thousandSeparator = ThousandsSeparator.SPACE
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("PARENTHESES . DECIMAL and , thousand separator")
            TransactionTextField(
                value = BigDecimal("-100000.00"),
                onValueChange = {
                    Log.d("Preview", "Value changed: $it")
                },
                isExpense = true,
                expenseFormat = ExpenseFormat.PARENTHESES
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("PARENTHESES with SPACE separator:")
            TransactionTextField(
                value = BigDecimal("-100000.00"),
                onValueChange = {
                    Log.d("Preview", "Value changed: $it")
                },
                isExpense = true,
                expenseFormat = ExpenseFormat.PARENTHESES,
                thousandSeparator = ThousandsSeparator.SPACE
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("PARENTHESES . decimal and thousand ,):")
            TransactionTextField(
                value = BigDecimal("-100000.00"),
                onValueChange = {
                    Log.d("Preview", "Value changed: $it")
                },
                isExpense = true,
                expenseFormat = ExpenseFormat.PARENTHESES,
                decimalSeparator = DecimalSeparator.COMMA,
                thousandSeparator = ThousandsSeparator.DOT
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("PARENTHESES , decimal and thousand .):")
            TransactionTextField(
                value = BigDecimal("-100000.00"),
                onValueChange = {
                    Log.d("Preview", "Value changed: $it")
                },
                isExpense = true,
                expenseFormat = ExpenseFormat.MINUS_SIGN,
                decimalSeparator = DecimalSeparator.COMMA,
                thousandSeparator = ThousandsSeparator.DOT
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Income with , Decimal and . Thousand:")
            TransactionTextField(
                value = BigDecimal("100000.00"),
                onValueChange = {
                    Log.d("Preview", "Value changed: $it")
                },
                isExpense = false,
                decimalSeparator = DecimalSeparator.COMMA,
                thousandSeparator = ThousandsSeparator.DOT
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Zero amount:")
            TransactionTextField(
                value = BigDecimal("0.00"),
                onValueChange = {
                    Log.d("Preview", "Value changed: $it")
                },
                isExpense = false
            )
        }
    }
}