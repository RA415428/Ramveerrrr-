package com.roxfollow.app.rewards

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class NativeRewardRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    suspend fun getCoinsPerRewardAd(): Int {
        val snapshot = firestore
            .collection("config")
            .document("global")
            .get()
            .await()

        val direct = (snapshot.get("coinsPerRewardAd") as? Number)?.toInt()
        if (direct != null && direct > 0) return direct

        val ads = snapshot.get("ads") as? Map<*, *>
        val nested = (ads?.get("coinsPerRewardAd") as? Number)?.toInt()

        return nested ?: 0
    }
}
