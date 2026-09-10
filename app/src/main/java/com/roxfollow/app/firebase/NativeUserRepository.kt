package com.roxfollow.app.firebase

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class NativeUserRepository {

    private val db = FirebaseFirestore.getInstance()

    suspend fun resolveMemberId(uid: String, email: String?): String? {

        // 1. Existing auth mapping
        val authDoc = db.collection("users_auth")
            .document(uid)
            .get()
            .await()

        val authMemberId = authDoc.getString("memberId")
        if (!authMemberId.isNullOrBlank()) {
            return authMemberId
        }

        // 2. Existing email mapping
        if (!email.isNullOrBlank()) {
            val safeEmail = email.trim().lowercase()

            val emailDoc = db.collection("email_accounts")
                .document(safeEmail)
                .get()
                .await()

            val emailMemberId = emailDoc.getString("memberId")
            if (!emailMemberId.isNullOrBlank()) {
                return emailMemberId
            }

            // 3. Existing user document by email
            val userQuery = db.collection("users")
                .whereEqualTo("email", email.trim())
                .limit(1)
                .get()
                .await()

            val userDoc = userQuery.documents.firstOrNull()
            val memberId = userDoc?.getString("memberId")
                ?: userDoc?.id

            if (!memberId.isNullOrBlank()) {
                return memberId
            }
        }

        return null
    }

    suspend fun loadUser(memberId: String): NativeUserData? {
        return FirestoreUserManager().getUser(memberId)
    }

    suspend fun linkAuth(
        memberId: String,
        uid: String,
        email: String?,
        displayName: String?
    ) {
        FirestoreUserManager().updateAuthLink(
            memberId = memberId,
            uid = uid,
            email = email,
            displayName = displayName
        )

        // Keep the existing users_auth collection compatible.
        val authData = hashMapOf<String, Any>(
            "memberId" to memberId,
            "uid" to uid
        )

        if (!email.isNullOrBlank()) {
            authData["email"] = email
        }

        db.collection("users_auth")
            .document(uid)
            .set(authData, com.google.firebase.firestore.SetOptions.merge())
            .await()
    }
}
