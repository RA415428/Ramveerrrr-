package com.roxfollow.app.rewards

import android.os.SystemClock

class RewardCooldown {
    companion object {
        private const val COOLDOWN_MS = 10_000L
    }

    private var cooldownUntil = 0L

    fun start() {
        cooldownUntil = SystemClock.elapsedRealtime() + COOLDOWN_MS
    }

    fun remainingSeconds(): Int {
        val remaining = cooldownUntil - SystemClock.elapsedRealtime()
        return if (remaining > 0) {
            ((remaining + 999) / 1000).toInt()
        } else {
            0
        }
    }

    fun isActive(): Boolean = remainingSeconds() > 0
}
