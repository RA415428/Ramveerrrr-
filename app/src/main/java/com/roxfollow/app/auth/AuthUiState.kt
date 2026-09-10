package com.roxfollow.app.auth

data class AuthUiState(
    val loading: Boolean = false,
    val loggedIn: Boolean = false,
    val uid: String? = null,
    val email: String? = null,
    val memberId: String? = null,
    val error: String? = null
)
