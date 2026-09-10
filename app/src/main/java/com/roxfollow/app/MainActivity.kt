package com.roxfollow.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.roxfollow.app.ads.UnityRewardedAdManager
import com.roxfollow.app.ui.AppRoot
import com.roxfollow.app.ui.theme.RoxFollowTheme

class MainActivity : ComponentActivity() {

    lateinit var unityRewardedAdManager: UnityRewardedAdManager
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        unityRewardedAdManager = UnityRewardedAdManager(
            activity = this,
            onRewarded = {
                // Reward will be connected to the existing coin system next.
            },
            onUnavailable = {
                // No reward on failure/cancel/not-ready.
            }
        )

        unityRewardedAdManager.initialize()

        setContent {
            RoxFollowTheme {
                AppRoot(rewardedAdManager = unityRewardedAdManager)
            }
        }
    }
}
