package com.roxfollow.app.rewards

import com.roxfollow.app.ads.UnityRewardedAdManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RewardedAdController(
    private val unityManager: UnityRewardedAdManager,
    private val memberIdProvider: () -> String,
    private val onRewardSuccess: (Int) -> Unit,
    private val onRewardFailed: () -> Unit
) {

    private val rewardService = FirestoreRewardService()
    private val cooldown = RewardCooldown()

    private var rewardInProgress = false

    fun show() {
        if (rewardInProgress || cooldown.isActive()) {
            return
        }

        rewardInProgress = true

        unityManager.showRewarded()
    }

    fun handleCompletedAd() {
        if (!rewardInProgress) return

        val memberId = memberIdProvider()

        CoroutineScope(Dispatchers.IO).launch {
            val coins = rewardService.getCoinsPerRewardAd()

            val success =
                if (coins > 0) {
                    rewardService.rewardUser(memberId, coins)
                } else {
                    false
                }

            withContext(Dispatchers.Main) {
                rewardInProgress = false

                if (success) {
                    cooldown.start()
                    onRewardSuccess(coins)
                } else {
                    onRewardFailed()
                }
            }
        }
    }

    fun handleAdUnavailable() {
        rewardInProgress = false
        onRewardFailed()
    }

    fun remainingCooldownSeconds(): Int {
        return cooldown.remainingSeconds()
    }
}
