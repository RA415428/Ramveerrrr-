package com.roxfollow.app.ui

import androidx.compose.runtime.Composable
import com.roxfollow.app.ui.screens.LoadingScreen
import com.roxfollow.app.ui.theme.RoxFollowTheme

@Composable
fun AppRoot() {
    RoxFollowTheme {
        LoadingScreen()
    }
}
