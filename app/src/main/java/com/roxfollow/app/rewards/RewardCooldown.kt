package com.roxfollow.app.rewards

import android.os.SystemClock

class RewardCooldown(
    private val durationMs: Long = 10_000L
) {
    private var untilElapsed = 0L

    fun start() {
        untilElapsed = SystemClock.elapsedRealtime() + durationMs
    }

    fun remainingSeconds(): Int {
        val remaining = untilElapsed - SystemClock.elapsedRealtime()
        if (remaining <= 0L) return 0
        return ((remaining + 999L) / 1000L).toInt()
    }

    fun isActive(): Boolean = remainingSeconds() > 0
}
