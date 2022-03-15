package com.cedrickflocon.android.showcase.search.domain

import arrow.core.Either

interface SearchUseCase {

    suspend fun search(query: String): Either<SearchError, SearchResult>

}