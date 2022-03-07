package com.cedrickflocon.android.showcase.user.domain

import arrow.core.Either
import com.google.common.truth.Truth.assertThat
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.coEvery
import io.mockk.mockk

class UserUseCaseImplSpec : DescribeSpec({
    val repository = mockk<UserRepository>()
    val useCase = UserUseCaseImpl(repository)

    describe("get user") {
        val user = mockk<Either<UserError, User>>()
        beforeEach { coEvery { repository.getUser("UserLogin") } returns user }

        it("should return the given user") {
            assertThat(useCase.getUser("UserLogin")).isEqualTo(user)
        }

    }

})
