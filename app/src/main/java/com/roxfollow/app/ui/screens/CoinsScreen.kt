package com.roxfollow.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.roxfollow.app.ads.UnityRewardedAdManager

@Composable
fun CoinsScreen(
    rewardedAdManager: UnityRewardedAdManager
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Current Coin Balance")

        Button(
            onClick = {
                rewardedAdManager.showRewarded()
            }
        ) {
            Text("Watch Ad + Coins")
        }
    }
}
