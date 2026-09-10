package com.roxfollow.app.firebase

import android.content.Context
import com.google.firebase.auth.FirebaseAuth

class AuthCoordinator(context: Context) {

    private val context = context.applicationContext
    private val auth = FirebaseAuth.getInstance()
    private val repository = NativeUserRepository(context)

    suspend fun signIn(
        email: String,
        password: String
    ): Result<NativeAuthResult> {

        return try {

            val cleanEmail = email.trim()

            if (cleanEmail.isEmpty()) {
                return Result.failure(Exception("Email is required"))
            }

            if (password.isEmpty()) {
                return Result.failure(Exception("Password is required"))
            }

            val result = auth
                .signInWithEmailAndPassword(cleanEmail, password)
                .awaitResult()

            val firebaseUser = result.user
                ?: return Result.failure(
                    Exception("Firebase user not found")
                )

            val memberUser = repository.resolveAndLoadUser(
                firebaseUser.uid,
                firebaseUser.email ?: cleanEmail
            )

            AuthSession.save(
                context,
                firebaseUser.uid,
                firebaseUser.email ?: cleanEmail,
                memberUser?.memberId
            )

            Result.success(
                NativeAuthResult(
                    uid = firebaseUser.uid,
                    email = firebaseUser.email ?: cleanEmail,
                    memberId = memberUser?.memberId
                )
            )

        } catch (e: Exception) {

            Result.failure(
                Exception(
                    AuthErrorMapper.message(e)
                )
            )
        }
    }


    suspend fun signUp(
        email: String,
        password: String,
        displayName: String
    ): Result<NativeAuthResult> {

        return try {

            val cleanEmail = email.trim()
            val cleanName = displayName.trim()

            if (cleanName.isEmpty()) {
                return Result.failure(
                    Exception("Name is required")
                )
            }

            if (cleanEmail.isEmpty()) {
                return Result.failure(
                    Exception("Email is required")
                )
            }

            if (password.length < 6) {
                return Result.failure(
                    Exception("Password must be at least 6 characters")
                )
            }

            val result = auth
                .createUserWithEmailAndPassword(
                    cleanEmail,
                    password
                )
                .awaitResult()

            val firebaseUser = result.user
                ?: return Result.failure(
                    Exception("Firebase user not found")
                )

            /*
             * Important:
             * Existing Firestore structure is NOT changed here.
             *
             * User/member creation and referral logic will be
             * connected in the dedicated Firestore migration step.
             */

            val memberUser = repository.resolveAndLoadUser(
                firebaseUser.uid,
                firebaseUser.email ?: cleanEmail
            )

            AuthSession.save(
                context,
                firebaseUser.uid,
                firebaseUser.email ?: cleanEmail,
                memberUser?.memberId
            )

            Result.success(
                NativeAuthResult(
                    uid = firebaseUser.uid,
                    email = firebaseUser.email ?: cleanEmail,
                    memberId = memberUser?.memberId
                )
            )

        } catch (e: Exception) {

            Result.failure(
                Exception(
                    AuthErrorMapper.message(e)
                )
            )
        }
    }


    fun currentUser(): NativeAuthResult? {

        val firebaseUser = auth.currentUser
            ?: return null

        val session = AuthSession.get(context)

        return NativeAuthResult(
            uid = firebaseUser.uid,
            email = firebaseUser.email ?: "",
            memberId = session.memberId
        )
    }


    fun signOut() {

        auth.signOut()

        AuthSession.clear(context)
    }
}


data class NativeAuthResult(
    val uid: String,
    val email: String,
    val memberId: String?
)
