package com.cedrickflocon.android.showcase.search.data

import arrow.core.left
import arrow.core.right
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Optional
import com.apollographql.apollo3.exception.ApolloException
import com.cedrickflocon.android.showcase.core.data.graphql.GraphQLClient
import com.cedrickflocon.android.showcase.search.domain.SearchError
import com.cedrickflocon.android.showcase.search.domain.SearchParams
import com.cedrickflocon.android.showcase.search.domain.SearchResult
import com.cedrickflocon.android.showcase.user.data.SearchQuery
import com.google.common.truth.Truth.assertThat
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import java.util.*

class SearchRepositoryImplSpec : DescribeSpec({
    val graphqlClient = mockk<GraphQLClient>()
    val mapper = mockk<SearchMapper>()
    val repository = SearchRepositoryImpl(graphqlClient, mapper)

    describe("search query") {
        val query = SearchQuery("Robert Cecil Martin", Optional.presentIfNotNull("Y3Vyc29yOjEw"))
        val searchParams = SearchParams("Robert Cecil Martin", "Y3Vyc29yOjEw")

        describe("data") {
            val searchResult = mockk<SearchResult>()
            beforeEach {
                val gqlSearch = mockk<SearchQuery.Search>()
                coEvery {
                    graphqlClient.client.query(query).execute()
                } returns ApolloResponse.Builder(
                    query,
                    UUID.randomUUID(),
                    mockk { every { search } returns gqlSearch }
                ).build()

                every { mapper(gqlSearch) } returns searchResult
            }

            it("return search result") {
                assertThat(repository.search(searchParams)).isEqualTo(searchResult.right())
            }
        }

        describe("no data") {
            beforeEach {
                coEvery {
                    graphqlClient.client.query(query).execute()
                } returns ApolloResponse.Builder(query, UUID.randomUUID(), null).build()
            }

            it("return search result") {
                assertThat(runCatching { repository.search(searchParams) }.exceptionOrNull())
                    .isInstanceOf(IllegalStateException::class.java)
            }
        }

        describe("exception") {
            describe("apollo exception") {
                beforeEach {
                    coEvery { graphqlClient.client.query(query) } throws ApolloException()
                }

                it("return network error") {
                    assertThat(repository.search(searchParams))
                        .isEqualTo(SearchError.Network.left())
                }
            }

            describe("unknown exception") {
                val exception = Exception()
                beforeEach {
                    coEvery { graphqlClient.client.query(query) } throws exception
                }

                it("should throw exception") {
                    assertThat(runCatching { repository.search(searchParams) }.exceptionOrNull())
                        .isEqualTo(exception)
                }
            }
        }
    }
})