package com.cedrickflocon.android.showcase.user.domain

import java.net.URI

data class User(
    val name: String,
    val avatarUrl: URI
)


sealed interface UserError {
    object Network : UserError
    object UserNotFound : UserError
}
