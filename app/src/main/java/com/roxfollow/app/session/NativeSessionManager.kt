package com.roxfollow.app.session

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.roxfollow.app.firebase.AuthSession

class NativeSessionManager(
    context: Context
) {

    private val appContext =
        context.applicationContext

    fun isSignedIn(): Boolean {

        return FirebaseAuth
            .getInstance()
            .currentUser != null
    }

    fun uid(): String? {

        return FirebaseAuth
            .getInstance()
            .currentUser
            ?.uid
    }

    fun email(): String? {

        return FirebaseAuth
            .getInstance()
            .currentUser
            ?.email
    }

    fun memberId(): String? {

        return AuthSession
            .get(appContext)
            .memberId
    }

    fun clear() {

        FirebaseAuth
            .getInstance()
            .signOut()

        AuthSession.clear(appContext)
    }
}
