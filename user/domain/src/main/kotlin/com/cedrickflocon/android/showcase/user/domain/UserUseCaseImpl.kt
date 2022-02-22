package com.cedrickflocon.android.showcase.user.domain

import arrow.core.Either
import arrow.core.right
import java.net.URL

class UserUseCaseImpl : UserUseCase {

    override suspend fun getUser(login: String): Either<UserError, User> {
        return User(login, URL("")).right()
    }

}
