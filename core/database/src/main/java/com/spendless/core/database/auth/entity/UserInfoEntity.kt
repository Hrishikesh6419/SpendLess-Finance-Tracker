package com.spendless.core.database.auth.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "user_info",
    indices = [
        Index(value = ["username"], unique = true), // Enforces unique usernames
        Index(value = ["userId"]) // Indexing primary key for fast lookups
    ]
)
data class UserInfoEntity(
    @PrimaryKey(autoGenerate = true)
    val userId: Long,
    val username: String,
    val pin: String
)
