package com.spendless.core.database.auth.utils

import com.hrishi.core.domain.auth.model.UserInfo
import com.hrishi.core.domain.security.EncryptionService
import com.spendless.core.database.auth.entity.UserInfoEntity

fun UserInfoEntity.toUserInfo(encryptionService: EncryptionService): UserInfo {
    return UserInfo(
        userId = this.userId,
        username = this.username,
        pin = encryptionService.decrypt(this.encryptedPin)
    )
}

fun UserInfo.toUserEntity(encryptionService: EncryptionService): UserInfoEntity {
    return UserInfoEntity(
        userId = this.userId ?: 0L,
        username = this.username,
        encryptedPin = encryptionService.encrypt(this.pin)
    )
}