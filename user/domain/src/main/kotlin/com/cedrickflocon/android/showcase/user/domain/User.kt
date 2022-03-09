package com.cedrickflocon.android.showcase.user.domain

import java.net.URI

data class User(
    val name: String?,
    val login: String,
    val avatarUrl: URI,
    val company: String?,
    val location: String?,
    val isHireable: Boolean,
    val followersCount: Int,
    val followingCount: Int,
)


sealed interface UserError {
    object Network : UserError
    object UserNotFound : UserError
}
