package com.roxfollow.app.firebase

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

class FirebaseAuthManager {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun currentUser(): FirebaseUser? {
        return auth.currentUser
    }

    fun isSignedIn(): Boolean {
        return auth.currentUser != null
    }

    suspend fun signIn(
        email: String,
        password: String
    ): Result<FirebaseUser> {
        return try {
            val result: AuthResult =
                auth.signInWithEmailAndPassword(
                    email.trim(),
                    password
                ).await()

            Result.success(
                result.user
                    ?: throw IllegalStateException("Firebase returned no user")
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signUp(
        email: String,
        password: String,
        displayName: String
    ): Result<FirebaseUser> {
        return try {
            val result: AuthResult =
                auth.createUserWithEmailAndPassword(
                    email.trim(),
                    password
                ).await()

            val user = result.user
                ?: throw IllegalStateException("Firebase returned no user")

            val profileUpdates =
                com.google.firebase.auth.UserProfileChangeRequest.Builder()
                    .setDisplayName(displayName.trim())
                    .build()

            user.updateProfile(profileUpdates).await()

            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun signOut() {
        auth.signOut()
    }

    fun authInstance(): FirebaseAuth {
        return auth
    }
}
