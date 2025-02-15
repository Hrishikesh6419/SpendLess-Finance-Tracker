package com.spendless.core.database.auth.utils

import com.hrishi.core.domain.auth.model.UserInfo
import com.spendless.core.database.auth.entity.UserInfoEntity

fun UserInfoEntity.toUserInfo(): UserInfo {
    return UserInfo(
        userId = this.userId,
        username = this.username,
        encryptedPin = this.encryptedPin,
        iv = this.iv
    )
}

fun UserInfo.toUserEntity(): UserInfoEntity {
    return UserInfoEntity(
        userId = this.userId ?: 0L,
        username = this.username,
        encryptedPin = this.encryptedPin,
        iv = this.iv
    )
}