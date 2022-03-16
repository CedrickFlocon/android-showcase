package com.cedrickflocon.android.showcase.user.router

import android.app.Activity
import android.content.Intent
import javax.inject.Inject

class UserRouter @Inject constructor(
    private val activity: Activity
) {

    companion object {
        private const val extra_login = "extra_login"

        fun readUserIntent(intent: Intent): UserParams {
            return UserParams(intent.getStringExtra(extra_login) ?: throw IllegalArgumentException("Extra $extra_login should not be null"))
        }
    }

    fun navigateToUser(userParams: UserParams) {
        activity.startActivity(Intent().apply {
            setClassName(activity.packageName, "com.cedrickflocon.android.showcase.user.presentation.UserActivity")
            putExtra(extra_login, userParams.login)
        })
    }

}
