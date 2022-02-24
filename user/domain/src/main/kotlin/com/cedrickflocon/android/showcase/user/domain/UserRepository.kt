package com.cedrickflocon.android.showcase.user.domain

import arrow.core.Either

interface UserRepository {

    suspend fun getUser(login: String): Either<UserError, User>

}
