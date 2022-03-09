package com.cedrickflocon.android.showcase.user.presentation

import arrow.core.left
import arrow.core.right
import com.cedrickflocon.android.showcase.user.domain.User
import com.cedrickflocon.android.showcase.user.domain.UserError
import com.cedrickflocon.android.showcase.user.domain.UserUseCase
import com.google.common.truth.Truth.assertThat
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import java.net.URI

class UserViewModelSpec : DescribeSpec({
    val useCase = mockk<UserUseCase>()
    val mapper = mockk<UiStateMapper>()
    val viewModel = UserViewModel(UserParams("cedrickflocon"), useCase, mapper)

    describe("get user on success") {
        val success = mockk<UserViewModel.UiState.Success>()
        beforeTest {
            val user = mockk<User>()
            coEvery { useCase.getUser("cedrickflocon") } returns user.right()
            every { mapper(user) } returns success
        }

        it("should have loading & success") {
            assertThat(viewModel.data.count()).isEqualTo(2)
            assertThat(viewModel.data.take(2).toList())
                .containsExactlyElementsIn(
                    listOf(
                        UserViewModel.UiState.Loading,
                        success
                    )
                )
        }
    }

    describe("get user on error") {
        beforeTest {
            coEvery { useCase.getUser("cedrickflocon") } returns UserError.UserNotFound.left()
        }

        it("should have loading & error") {
            assertThat(viewModel.data.count()).isEqualTo(2)
            assertThat(viewModel.data.take(2).toList())
                .containsExactlyElementsIn(
                    listOf(
                        UserViewModel.UiState.Loading,
                        UserViewModel.UiState.Error("error")
                    )
                )
        }
    }

})
