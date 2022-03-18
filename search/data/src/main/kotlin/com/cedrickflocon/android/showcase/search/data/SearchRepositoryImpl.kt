package com.cedrickflocon.android.showcase.search.data

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.apollographql.apollo3.exception.ApolloException
import com.cedrickflocon.android.showcase.core.data.graphql.GraphQLClient
import com.cedrickflocon.android.showcase.search.domain.SearchError
import com.cedrickflocon.android.showcase.search.domain.SearchRepository
import com.cedrickflocon.android.showcase.search.domain.SearchResult
import com.cedrickflocon.android.showcase.user.data.SearchQuery
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val graphQLClient: GraphQLClient,
    private val mapper: SearchMapper,
) : SearchRepository {

    override suspend fun search(query: String): Either<SearchError, SearchResult> {
        val response = try {
            graphQLClient.client.query(SearchQuery(query)).execute()
        } catch (e: ApolloException) {
            return SearchError.Network.left()
        }

        return response.data?.search
            ?.let { mapper(it).right() }
            ?: throw IllegalStateException()
    }

}
