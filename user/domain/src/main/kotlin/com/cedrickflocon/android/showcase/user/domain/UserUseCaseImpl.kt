package com.cedrickflocon.android.showcase.user.domain

import arrow.core.Either
import arrow.core.right
import java.net.URL

class UserUseCaseImpl : UserUseCase {

    override suspend fun getUser(login: String): Either<UserError, User> {
        return User(login, URL("https://avatars.githubusercontent.com/u/7161169?s=96&v=4")).right()
    }

}
