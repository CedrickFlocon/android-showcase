package com.cedrickflocon.android.showcase.user.domain

import arrow.core.Either
import arrow.core.right
import kotlinx.coroutines.delay
import java.net.URI
import java.net.URL

class UserUseCaseImpl : UserUseCase {

    override suspend fun getUser(login: String): Either<UserError, User> {
        delay(5000)
        return User(login, URI("https://avatars.githubusercontent.com/u/7161169?s=96&v=4")).right()
    }

}
