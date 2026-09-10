package com.roxfollow.app.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/*
 * Native Loading Screen
 *
 * IMPORTANT:
 * This is only the migration foundation.
 * Existing React UI has NOT been redesigned.
 *
 * Exact logo, text, spacing, colors and loading behavior
 * will be ported from the original project in the next
 * UI migration pass.
 */
@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}
