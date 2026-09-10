package com.roxfollow.app

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.roxfollow.app.ads.UnityRewardedAdManager
import com.roxfollow.app.rewards.RewardedAdController
import com.roxfollow.app.ui.AppRoot
import com.roxfollow.app.ui.theme.RoxFollowTheme

class MainActivity : ComponentActivity() {

    lateinit var unityRewardedAdManager: UnityRewardedAdManager
        private set

    private lateinit var rewardedAdController: RewardedAdController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lateinit var manager: UnityRewardedAdManager

        manager = UnityRewardedAdManager(
            activity = this,
            onRewarded = {
                rewardedAdController.handleCompletedAd()
            },
            onUnavailable = {
                rewardedAdController.handleAdUnavailable()
            }
        )

        unityRewardedAdManager = manager

        rewardedAdController = RewardedAdController(
            unityManager = manager,
            memberIdProvider = {
                getSharedPreferences("rox_follow_auth", MODE_PRIVATE)
                    .getString("memberId", "") ?: ""
            },
            onRewardSuccess = { coins ->
                Log.d("ROX_REWARD", "Reward credited: $coins coins")
            },
            onRewardFailed = {
                Log.d("ROX_REWARD", "Reward failed - 0 coins")
            }
        )

        manager.initialize()

        setContent {
            RoxFollowTheme {
                AppRoot()
            }
        }
    }
}
