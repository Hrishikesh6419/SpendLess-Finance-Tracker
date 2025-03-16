package com.hrishi.core.data.time

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.android.gms.time.TrustedTime
import com.google.android.gms.time.TrustedTimeClient
import com.hrishi.core.domain.time.TimeProvider
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@RequiresApi(Build.VERSION_CODES.O)
class TrustedTimeProvider(
    context: Context,
    private val zoneId: ZoneId
) : TimeProvider {
    private var trustedTimeClient: TrustedTimeClient? = null

    init {
        TrustedTime.createClient(context).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                trustedTimeClient = task.result
            }
        }
    }

    override val currentLocalDateTime: LocalDateTime
        get() = trustedTimeClient?.computeCurrentInstant()?.atZone(zoneId)?.toLocalDateTime()
            ?: Instant.now().atZone(zoneId).toLocalDateTime()
}
