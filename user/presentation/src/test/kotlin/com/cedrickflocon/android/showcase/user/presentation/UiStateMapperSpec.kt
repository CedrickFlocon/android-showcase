package com.cedrickflocon.android.showcase.user.presentation

import com.cedrickflocon.android.showcase.user.domain.User
import com.google.common.truth.Truth.*
import io.kotest.core.spec.style.DescribeSpec
import java.net.URI

class UiStateMapperSpec : DescribeSpec({
    val mapper = UiStateMapper()

    describe("user") {
        val user = User(
            name = "Cedrick",
            login = "CedrickFlocon",
            avatarUrl = URI("http://cedrickflocon"),
            company = "Best Company",
            location = "Paris",
            isHireable = true,
            followersCount = 832480,
            followingCount = 432879,
        )

        it("can be transform to a success") {
            assertThat(mapper(user))
                .isEqualTo(
                    UserViewModel.UiState.Success(
                        name = "Cedrick",
                        login = "CedrickFlocon",
                        avatarUrl = URI("http://cedrickflocon"),
                        company = "Best Company",
                        location = "Paris",
                        isHireable = true,
                        followersCount = 832480,
                        followingCount = 432879,
                    )
                )
        }
    }
})
