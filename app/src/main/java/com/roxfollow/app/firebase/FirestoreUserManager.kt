package com.roxfollow.app.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

data class NativeUserData(
    val memberId: String? = null,
    val email: String? = null,
    val displayName: String? = null,
    val coins: Long = 0L,
    val referralCode: String? = null,
    val status: String = "ACTIVE",
    val authUid: String? = null
)

class FirestoreUserManager {

    private val db = FirebaseFirestore.getInstance()

    suspend fun findByAuthUid(uid: String): NativeUserData? {
        val snapshot = db.collection("users")
            .whereEqualTo("authUid", uid)
            .limit(1)
            .get()
            .await()

        val doc = snapshot.documents.firstOrNull() ?: return null
        return fromDocument(doc.id, doc.data ?: emptyMap())
    }

    suspend fun findByEmail(email: String): NativeUserData? {
        val snapshot = db.collection("users")
            .whereEqualTo("email", email.trim())
            .limit(1)
            .get()
            .await()

        val doc = snapshot.documents.firstOrNull() ?: return null
        return fromDocument(doc.id, doc.data ?: emptyMap())
    }

    suspend fun getUser(memberId: String): NativeUserData? {
        val doc = db.collection("users")
            .document(memberId)
            .get()
            .await()

        if (!doc.exists()) return null
        return fromDocument(doc.id, doc.data ?: emptyMap())
    }

    suspend fun updateAuthLink(
        memberId: String,
        uid: String,
        email: String?,
        displayName: String?
    ) {
        val data = hashMapOf<String, Any>(
            "authUid" to uid,
            "status" to "ACTIVE"
        )

        if (!email.isNullOrBlank()) data["email"] = email
        if (!displayName.isNullOrBlank()) data["displayName"] = displayName

        db.collection("users")
            .document(memberId)
            .set(data, SetOptions.merge())
            .await()
    }

    private fun fromDocument(
        documentId: String,
        data: Map<String, Any>
    ): NativeUserData {
        val memberId = (data["memberId"] as? String)?.ifBlank { null }
            ?: documentId

        val coins = when (val value = data["coins"]) {
            is Number -> value.toLong()
            else -> 0L
        }

        return NativeUserData(
            memberId = memberId,
            email = data["email"] as? String,
            displayName = data["displayName"] as? String,
            coins = coins,
            referralCode = data["referralCode"] as? String,
            status = data["status"] as? String ?: "ACTIVE",
            authUid = data["authUid"] as? String
        )
    }
}
