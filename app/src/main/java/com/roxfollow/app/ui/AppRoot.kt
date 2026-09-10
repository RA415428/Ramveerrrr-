package com.roxfollow.app.ui

import androidx.compose.runtime.Composable
import com.roxfollow.app.ads.UnityRewardedAdManager
import com.roxfollow.app.ui.screens.CoinsScreen
import com.roxfollow.app.ui.theme.RoxFollowTheme

@Composable
fun AppRoot(
    rewardedAdManager: UnityRewardedAdManager
) {
    RoxFollowTheme {
        CoinsScreen(
            rewardedAdManager = rewardedAdManager
        )
    }
}
