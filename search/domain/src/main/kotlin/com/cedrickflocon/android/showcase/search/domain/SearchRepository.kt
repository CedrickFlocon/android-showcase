package com.cedrickflocon.android.showcase.search.domain

import arrow.core.Either


interface SearchRepository {

    suspend fun search(searchParams: SearchParams, cursor: String?): Either<SearchError, SearchResult>

}
