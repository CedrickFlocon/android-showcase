package com.cedrickflocon.android.showcase.user.data

import com.cedrickflocon.android.showcase.user.domain.User
import com.google.common.truth.Truth.assertThat
import io.kotest.core.spec.style.DescribeSpec
import java.net.URI

class UserMapperSpec : DescribeSpec({
    val mapper = UserMapper()

    describe("gql user") {
        val gqlUser = GetUserQuery.User(
            name = "Cedrick",
            login = "CedrickFlocon",
            avatarUrl = URI("http://cedrickflocon"),
            company = "Best Company",
            location = "Paris",
            isHireable = true,
            followers = GetUserQuery.Followers(832480),
            following = GetUserQuery.Following(432879),
        )

        it("can be transform to a user") {
            assertThat(mapper(gqlUser))
                .isEqualTo(
                    User(
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