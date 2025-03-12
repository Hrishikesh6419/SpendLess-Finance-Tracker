package com.spendless.core.database.transactions.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.hrishi.core.domain.model.TransactionCategory
import com.hrishi.core.domain.model.TransactionType
import com.spendless.core.database.auth.entity.UserInfoEntity
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity(
    tableName = "transactions",
    foreignKeys = [
        ForeignKey(
            entity = UserInfoEntity::class,
            parentColumns = ["userId"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["userId"]), Index(value = ["recurringTransactionId"])]
)
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val transactionId: Long,
    val userId: Long,
    val transactionType: TransactionType,
    val transactionNameEncrypted: String, // Encrypt || ✅ Done
    val amount: BigDecimal, // Encrypt ☠️
    val noteEncrypted: String?, // Encrypt || ✅ Done
    val transactionCategory: TransactionCategory, // Encrypt ☠️
    val transactionDate: LocalDateTime,
    val recurringStartDate: LocalDateTime,
    val recurringTransactionId: Long?,
    val recurringTypeEncrypted: String, // Encrypt || ✅ Done
    val nextRecurringDate: LocalDateTime?,
)