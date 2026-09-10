package com.roxfollow.app.firebase

import android.content.Context

data class SavedAuthSession(
    val uid: String?,
    val email: String?,
    val memberId: String?
)

object AuthSession {

    private const val PREFS = "rox_follow_auth_session"

    private const val KEY_UID = "uid"
    private const val KEY_EMAIL = "email"
    private const val KEY_MEMBER_ID = "member_id"

    fun save(
        context: Context,
        uid: String,
        email: String,
        memberId: String?
    ) {

        context
            .getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_UID, uid)
            .putString(KEY_EMAIL, email)
            .putString(KEY_MEMBER_ID, memberId)
            .apply()
    }


    fun get(context: Context): SavedAuthSession {

        val prefs =
            context.getSharedPreferences(
                PREFS,
                Context.MODE_PRIVATE
            )

        return SavedAuthSession(
            uid = prefs.getString(KEY_UID, null),
            email = prefs.getString(KEY_EMAIL, null),
            memberId = prefs.getString(KEY_MEMBER_ID, null)
        )
    }


    fun clear(context: Context) {

        context
            .getSharedPreferences(
                PREFS,
                Context.MODE_PRIVATE
            )
            .edit()
            .clear()
            .apply()
    }
}
