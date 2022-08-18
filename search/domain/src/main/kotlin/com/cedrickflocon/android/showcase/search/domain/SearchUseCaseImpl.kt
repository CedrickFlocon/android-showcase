package com.cedrickflocon.android.showcase.search.domain

import arrow.core.Either
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class SearchUseCaseImpl @Inject constructor(
    private val repository: SearchRepository,
    private val externalScope: CoroutineScope
) : SearchUseCase {

    private val initialState = State(
        searchParams = null,
        nextCursor = null,
        error = false,
        loading = false,
        items = null
    )

    override val events = MutableSharedFlow<SearchUseCase.Event>()
    private val state = MutableStateFlow(initialState)

    override val search = events
        .filter { it.isValid(state.value) }
        .flatMapLatest {
            flow {
                when (it) {
                    is SearchUseCase.Event.NewSearch -> {
                        val newState = initialState.onLoading(it.searchParams)
                        emit(newState)
                        search(it.searchParams, null).fold(
                            { emit(newState.onError()) },
                            { emit(newState.onSuccess(it)) }
                        )
                    }
                    SearchUseCase.Event.Retry -> {
                        val newState = state.value.onLoading(state.value.searchParams!!)
                        emit(newState)
                        search(newState.searchParams!!, newState.nextCursor).fold(
                            { emit(newState.onError()) },
                            { emit(newState.onSuccess(it)) }
                        )
                    }
                    SearchUseCase.Event.NextPage -> {
                        val newState = state.value.onLoading(state.value.searchParams!!)
                        emit(newState)
                        search(newState.searchParams!!, newState.nextCursor).fold(
                            { emit(newState.onError()) },
                            { emit(newState.onSuccess(it)) }
                        )
                    }
                }
            }
        }
        .onEach { state.emit(it) }
        .map {
            SearchUseCase.State(
                searchParams = it.searchParams!!,
                hasNext = it.nextCursor != null,
                error = it.error,
                loading = it.loading,
                items = it.items
            )
        }
        .distinctUntilChanged()
        .shareIn(externalScope, SharingStarted.Eagerly, 1)

    private fun State.onLoading(newSearchParams: SearchParams) = this.copy(
        searchParams = newSearchParams,
        loading = true,
        error = false
    )

    private fun State.onError() = this.copy(
        error = true,
        loading = false
    )

    private fun State.onSuccess(searchResult: SearchResult) = this.copy(
        error = false,
        loading = false,
        items = (this.items ?: emptyList()) + searchResult.searchResultItem,
        nextCursor = searchResult.pageInfo.nextCursor
    )

    private fun SearchUseCase.Event.isValid(state: State): Boolean {
        return when (this) {
            is SearchUseCase.Event.NewSearch -> true
            SearchUseCase.Event.NextPage -> state.nextCursor != null && !state.loading && !state.error
            SearchUseCase.Event.Retry -> state.error
        }
    }

    private suspend fun search(searchParams: SearchParams, cursor: String?): Either<SearchError, SearchResult> {
        return repository.search(searchParams, cursor)
    }

    private data class State(
        val searchParams: SearchParams?,
        val nextCursor: String?,
        val error: Boolean,
        val loading: Boolean,
        val items: List<SearchResultItem>?,
    )

}
