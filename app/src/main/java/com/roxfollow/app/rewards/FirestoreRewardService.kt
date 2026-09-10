package com.roxfollow.app.rewards

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.UUID

class FirestoreRewardService(

    suspend fun getCoinsPerRewardAd(): Int {
        val snapshot = firestore.collection("config").document("global").get().await()
        val direct = (snapshot.get("coinsPerRewardAd") as? Number)?.toInt()
        if (direct != null && direct > 0) return direct
        val ads = snapshot.get("ads") as? Map<*, *>
        return (ads?.get("coinsPerRewardAd") as? Number)?.toInt() ?: 0
    }
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    suspend fun rewardUser(
        memberId: String,
        coinsToAdd: Int
    ): Boolean {
        if (memberId.isBlank() || coinsToAdd <= 0) return false

        val userRef = firestore
            .collection("users")
            .document(memberId)

        val transactionId = "reward_ad_${UUID.randomUUID()}"

        val transactionRef = firestore
            .collection("coinTransactions")
            .document(transactionId)

        return try {
            firestore.runTransaction { transaction ->

                val userSnapshot = transaction.get(userRef)

                val currentCoins =
                    (userSnapshot.get("coins") as? Number)?.toLong() ?: 0L

                val newCoins = currentCoins + coinsToAdd

                transaction.update(
                    userRef,
                    "coins",
                    newCoins
                )

                transaction.set(
                    transactionRef,
                    mapOf(
                        "memberId" to memberId,
                        "amount" to coinsToAdd,
                        "type" to "rewarded_ad",
                        "description" to "Unity rewarded ad",
                        "balanceBefore" to currentCoins,
                        "balanceAfter" to newCoins,
                        "createdAt" to FieldValue.serverTimestamp()
                    )
                )

                true
            }.await()
        } catch (e: Exception) {
            false
        }
    }
}
