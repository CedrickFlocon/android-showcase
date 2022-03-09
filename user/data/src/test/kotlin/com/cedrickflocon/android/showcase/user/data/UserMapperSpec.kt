package com.cedrickflocon.android.showcase.user.data

import com.cedrickflocon.android.showcase.user.domain.User
import com.google.common.truth.Truth.assertThat
import io.kotest.core.spec.style.DescribeSpec
import java.net.URI

class UserMapperSpec : DescribeSpec({
    val mapper = UserMapper()

    describe("gql user") {
        val gqlUser = GetUserQuery.User("Cedrick", URI("http://cedrickflocon"))

        it("can be transform to a user") {
            assertThat(mapper(gqlUser))
                .isEqualTo(User("Cedrick", URI("http://cedrickflocon")))
        }
    }
})