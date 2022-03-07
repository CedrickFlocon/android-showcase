package com.cedrickflocon.android.showcase.user.domain

import arrow.core.Either
import arrow.core.right
import kotlinx.coroutines.delay
import java.net.URI
import javax.inject.Inject

class UserUseCaseImpl @Inject constructor(
    private val repository: UserRepository
) : UserUseCase {

    override suspend fun getUser(login: String): Either<UserError, User> {
        return repository.getUser(login)
    }

}
