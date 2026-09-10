package com.roxfollow.app.ads

import android.app.Activity
import android.util.Log
import com.unity3d.ads.IUnityAdsInitializationListener
import com.unity3d.ads.IUnityAdsLoadListener
import com.unity3d.ads.IUnityAdsShowListener
import com.unity3d.ads.UnityAds
import com.unity3d.ads.UnityAdsShowOptions

class UnityRewardedAdManager(
    private val activity: Activity,
    private val onRewarded: () -> Unit,
    private val onUnavailable: () -> Unit
) {

    companion object {
        private const val TAG = "UnityRewardedAds"
        private const val GAME_ID = "800368206"
        private const val AD_UNIT_ID = "Rewarded_Android"
        private const val TEST_MODE = false
    }

    private var initialized = false
    private var loading = false

    private val loadListener = object : IUnityAdsLoadListener {
        override fun onUnityAdsAdLoaded(placementId: String) {
            loading = false

            if (placementId == AD_UNIT_ID) {
                UnityAds.show(
                    activity,
                    AD_UNIT_ID,
                    UnityAdsShowOptions(),
                    showListener
                )
            }
        }

        override fun onUnityAdsFailedToLoad(
            placementId: String,
            error: UnityAds.UnityAdsLoadError,
            message: String
        ) {
            loading = false
            Log.e(TAG, "Load failed: $error - $message")
            onUnavailable()
        }
    }

    private val showListener = object : IUnityAdsShowListener {
        override fun onUnityAdsShowFailure(
            placementId: String,
            error: UnityAds.UnityAdsShowError,
            message: String
        ) {
            Log.e(TAG, "Show failed: $error - $message")
            onUnavailable()
        }

        override fun onUnityAdsShowStart(placementId: String) {
            Log.d(TAG, "Ad started")
        }

        override fun onUnityAdsShowClick(placementId: String) {
            Log.d(TAG, "Ad clicked")
        }

        override fun onUnityAdsShowComplete(
            placementId: String,
            state: UnityAds.UnityAdsShowCompletionState
        ) {
            if (
                placementId == AD_UNIT_ID &&
                state == UnityAds.UnityAdsShowCompletionState.COMPLETED
            ) {
                Log.d(TAG, "REWARDED: ad completed")
                onRewarded()
            } else {
                Log.d(TAG, "No reward: $state")
                onUnavailable()
            }
        }
    }

    fun initialize() {
        if (initialized || UnityAds.isInitialized) {
            initialized = true
            return
        }

        UnityAds.initialize(
            activity.applicationContext,
            GAME_ID,
            TEST_MODE,
            object : IUnityAdsInitializationListener {
                override fun onInitializationComplete() {
                    initialized = true
                    Log.d(TAG, "Unity Ads initialized")
                }

                override fun onInitializationFailed(
                    error: UnityAds.UnityAdsInitializationError,
                    message: String
                ) {
                    initialized = false
                    Log.e(TAG, "Initialization failed: $error - $message")
                }
            }
        )
    }

    fun showRewarded() {
        if (!initialized && !UnityAds.isInitialized) {
            onUnavailable()
            return
        }

        if (loading || UnityAds.isShowing) {
            onUnavailable()
            return
        }

        loading = true
        UnityAds.load(AD_UNIT_ID, loadListener)
    }
}
