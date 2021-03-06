package com.cedrickflocon.android.showcase.search.domain

import arrow.core.Either

interface SearchUseCase {

    suspend fun search(searchParams: SearchParams, cursor: String?): Either<SearchError, SearchResult>

}