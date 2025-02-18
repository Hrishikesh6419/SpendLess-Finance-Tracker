package com.spendless.core.database.preferences.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.hrishi.core.domain.model.Currency
import com.hrishi.core.domain.model.DecimalSeparator
import com.hrishi.core.domain.model.ExpenseFormat
import com.hrishi.core.domain.model.LockoutDuration
import com.hrishi.core.domain.model.PinAttempts
import com.hrishi.core.domain.model.SessionDuration
import com.hrishi.core.domain.model.ThousandsSeparator
import com.spendless.core.database.auth.entity.UserInfoEntity
import com.spendless.core.database.preferences.utils.UserPreferenceConverters

@Entity(
    tableName = "user_preference",
    foreignKeys = [
        ForeignKey(
            entity = UserInfoEntity::class,
            parentColumns = ["userId"], // References `userId` from UserInfoEntity
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE // Deletes preferences if user is deleted
        )
    ],
    indices = [Index(value = ["userId"])]
)
@TypeConverters(UserPreferenceConverters::class)
data class UserPreferenceEntity(
    @PrimaryKey val userId: Long, // Primary Key & Foreign Key
    val expenseFormat: ExpenseFormat,
    val currency: Currency,
    val decimalSeparator: DecimalSeparator,
    val thousandsSeparator: ThousandsSeparator,
    val isBiometricEnabled: Boolean,
    val sessionDuration: SessionDuration,
    val lockOutDuration: LockoutDuration,
    val allowedPinAttempts: PinAttempts
)