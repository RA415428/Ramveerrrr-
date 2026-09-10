package com.roxfollow.app.rewards

sealed class RewardResult {
    data class Success(val coins: Int) : RewardResult()
    data object Failed : RewardResult()
}
