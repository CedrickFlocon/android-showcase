package com.cedrickflocon.android.showcase.user.domain

import java.net.URL

data class User(
    val name: String,
    val avatarUrl: URL
)


sealed interface UserError {
    object UserNotFound : UserError
}
