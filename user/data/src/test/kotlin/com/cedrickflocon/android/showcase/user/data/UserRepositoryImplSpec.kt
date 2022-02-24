package com.cedrickflocon.android.showcase.user.data

import arrow.core.left
import arrow.core.right
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.exception.ApolloException
import com.cedrickflocon.android.showcase.user.domain.User
import com.cedrickflocon.android.showcase.user.domain.UserError
import com.google.common.truth.Truth.assertThat
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.coEvery
import io.mockk.mockk
import java.net.URI

class UserRepositoryImplSpec : DescribeSpec({
    val client = mockk<ApolloClient>()
    val repository = UserRepositoryImpl(client)

    describe("get user on success") {
        beforeTest {
            coEvery {
                client.query(GetUserQuery("cedrickflocon")).execute().dataAssertNoErrors.user
            } returns GetUserQuery.User("Cedrick", URI("http://cedrickflocon"))
        }

        it("return user") {
            assertThat(repository.getUser("cedrickflocon"))
                .isEqualTo(User("Cedrick", URI("http://cedrickflocon")).right())
        }
    }


    describe("get user on error") {
        beforeTest {
            coEvery { client.query(GetUserQuery("cedrickflocon")) } throws ApolloException()
        }

        it("return error") {
            assertThat(repository.getUser("cedrickflocon"))
                .isEqualTo(UserError.Network.left())
        }
    }
})
