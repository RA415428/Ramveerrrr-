package com.roxfollow.app.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.roxfollow.app.firebase.AuthCoordinator
import com.roxfollow.app.firebase.AuthSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val coordinator =
        AuthCoordinator(application.applicationContext)

    private val _state =
        MutableStateFlow(AuthUiState())

    val state: StateFlow<AuthUiState> =
        _state.asStateFlow()

    init {
        restoreSession()
    }

    fun restoreSession() {

        val firebaseUser =
            FirebaseAuth.getInstance().currentUser

        val saved =
            AuthSession.get(
                getApplication<Application>()
                    .applicationContext
            )

        if (firebaseUser != null) {

            _state.value = AuthUiState(
                loading = false,
                loggedIn = true,
                uid = firebaseUser.uid,
                email = firebaseUser.email ?: saved.email,
                memberId = saved.memberId
            )

        } else {

            AuthSession.clear(
                getApplication<Application>()
                    .applicationContext
            )

            _state.value = AuthUiState()
        }
    }

    fun signIn(
        email: String,
        password: String,
        onSuccess: () -> Unit = {}
    ) {

        if (_state.value.loading) return

        viewModelScope.launch {

            _state.value =
                _state.value.copy(
                    loading = true,
                    error = null
                )

            val result =
                coordinator.signIn(
                    email = email,
                    password = password
                )

            result.fold(

                onSuccess = {

                    _state.value =
                        AuthUiState(
                            loading = false,
                            loggedIn = true,
                            uid = it.uid,
                            email = it.email,
                            memberId = it.memberId
                        )

                    onSuccess()
                },

                onFailure = {

                    _state.value =
                        _state.value.copy(
                            loading = false,
                            error = it.message
                                ?: "Authentication failed."
                        )
                }
            )
        }
    }

    fun signUp(
        email: String,
        password: String,
        displayName: String,
        onSuccess: () -> Unit = {}
    ) {

        if (_state.value.loading) return

        viewModelScope.launch {

            _state.value =
                _state.value.copy(
                    loading = true,
                    error = null
                )

            val result =
                coordinator.signUp(
                    email = email,
                    password = password,
                    displayName = displayName
                )

            result.fold(

                onSuccess = {

                    _state.value =
                        AuthUiState(
                            loading = false,
                            loggedIn = true,
                            uid = it.uid,
                            email = it.email,
                            memberId = it.memberId
                        )

                    onSuccess()
                },

                onFailure = {

                    _state.value =
                        _state.value.copy(
                            loading = false,
                            error = it.message
                                ?: "Account creation failed."
                        )
                }
            )
        }
    }

    fun clearError() {

        _state.value =
            _state.value.copy(
                error = null
            )
    }

    fun signOut() {

        coordinator.signOut()

        _state.value =
            AuthUiState()
    }
}
