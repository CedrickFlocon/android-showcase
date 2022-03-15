package com.cedrickflocon.android.showcase.search.domain

import arrow.core.Either


interface SearchRepository {

    suspend fun search(query: String): Either<SearchError, SearchResult>

}
