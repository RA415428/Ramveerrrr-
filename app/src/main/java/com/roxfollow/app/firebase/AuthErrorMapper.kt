package com.roxfollow.app.firebase

import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException

object AuthErrorMapper {

    fun message(error: Throwable): String {

        return when (error) {

            is FirebaseAuthInvalidUserException ->
                "Account not found. Please check your email."

            is FirebaseAuthInvalidCredentialsException ->
                "Invalid email or password."

            is FirebaseAuthUserCollisionException ->
                "An account already exists with this email."

            else ->
                error.message ?: "Authentication failed."
        }
    }
}
