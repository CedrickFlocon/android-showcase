package com.cedrickflocon.android.showcase.user.data

import arrow.core.left
import arrow.core.right
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.exception.ApolloException
import com.cedrickflocon.android.showcase.user.domain.User
import com.cedrickflocon.android.showcase.user.domain.UserError
import com.google.common.truth.Truth.assertThat
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import java.util.*
import com.apollographql.apollo3.api.Error as GraphqlError

class UserRepositoryImplSpec : DescribeSpec({
    val graphqlClient = mockk<GraphQLClient>()
    val mapper = mockk<UserMapper>()
    val repository = UserRepositoryImpl(graphqlClient, mapper)

    describe("get user") {
        val query = GetUserQuery("cedrickflocon")

        describe("data") {
            lateinit var data: GetUserQuery.Data
            beforeEach {
                data = mockk()
                coEvery {
                    graphqlClient.client.query(query).execute()
                } returns ApolloResponse.Builder(query, UUID.randomUUID(), data).build()
            }

            describe("success") {
                val user = mockk<User>()
                beforeEach {
                    val gqlUser = mockk<GetUserQuery.User>()
                    every { data.user } returns gqlUser
                    every { mapper(gqlUser) } returns user
                }

                it("return user") {
                    assertThat(repository.getUser("cedrickflocon"))
                        .isEqualTo(user.right())
                }
            }

            describe("no data") {
                beforeEach { every { data.user } returns null }

                it("throw exception") {
                    assertThat(runCatching { repository.getUser("cedrickflocon") }.exceptionOrNull())
                        .isInstanceOf(IllegalArgumentException::class.java)
                }
            }
        }

        describe("error") {
            lateinit var errors: MutableList<GraphqlError>
            beforeEach {
                errors = mutableListOf()
                coEvery {
                    graphqlClient.client.query(query).execute()
                } returns ApolloResponse.Builder(query, UUID.randomUUID(), mockk()).errors(errors).build()
            }

            describe("user not found") {
                beforeEach {
                    errors.add(mockk {
                        every { nonStandardFields } returns mapOf(
                            "" to "",
                            "type" to "NOT_FOUND",
                        )
                    })
                }

                it("return user not found") {
                    assertThat(repository.getUser("cedrickflocon"))
                        .isEqualTo(UserError.UserNotFound.left())
                }
            }

            describe("unknown error") {
                beforeEach {
                    errors.add(mockk {
                        every { nonStandardFields } returns mapOf(
                            "type" to "UNKNOWN",
                            "unknown" to "NOT_FOUND",
                        )
                    })
                }

                it("should throw exception") {
                    assertThat(runCatching { repository.getUser("cedrickflocon") }.exceptionOrNull())
                        .isInstanceOf(IllegalArgumentException::class.java)
                }
            }
        }

        describe("exception") {
            describe("apollo exception") {
                beforeEach {
                    coEvery { graphqlClient.client.query(query) } throws ApolloException()
                }

                it("return network error") {
                    assertThat(repository.getUser("cedrickflocon"))
                        .isEqualTo(UserError.Network.left())
                }
            }

            describe("unknown exception") {
                val exception = Exception()
                beforeEach {
                    coEvery { graphqlClient.client.query(query) } throws exception
                }

                it("should throw exception") {
                    assertThat(runCatching { repository.getUser("cedrickflocon") }.exceptionOrNull())
                        .isEqualTo(exception)
                }
            }
        }
    }
})
