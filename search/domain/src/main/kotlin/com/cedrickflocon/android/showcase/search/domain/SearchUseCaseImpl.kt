package com.cedrickflocon.android.showcase.search.domain

import arrow.core.Either
import javax.inject.Inject

class SearchUseCaseImpl @Inject constructor(
    private val repository: SearchRepository
) : SearchUseCase {

    override suspend fun search(query: String): Either<SearchError, SearchResult> {
        return repository.search(query)
    }

}
