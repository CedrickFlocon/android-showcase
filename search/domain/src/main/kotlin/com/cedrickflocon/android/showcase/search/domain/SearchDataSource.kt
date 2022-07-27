package com.cedrickflocon.android.showcase.search.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchDataSource @Inject constructor(
    private val useCase: SearchUseCase,
    private val externalScope: CoroutineScope
) {

    private val initialState = State(
        searchParams = SearchParams(query = ""),
        nextCursor = null,
        error = false,
        loading = false,
        items = null
    )

    val events = MutableSharedFlow<Event>()
    private val state = MutableStateFlow(initialState)

    val data = events
        .filter { it.isValid(state.value) }
        .flatMapLatest {
            flow {
                when (it) {
                    is Event.NewSearch -> {
                        val newState = initialState.onLoading(it.searchParams)
                        emit(newState)
                        useCase.search(it.searchParams, null).fold(
                            { emit(newState.onError()) },
                            { emit(newState.onSuccess(it)) }
                        )
                    }
                    Event.Retry -> {
                        val newState = state.value.onLoading(state.value.searchParams)
                        emit(newState)
                        useCase.search(newState.searchParams, newState.nextCursor).fold(
                            { emit(newState.onError()) },
                            { emit(newState.onSuccess(it)) }
                        )
                    }
                    Event.NextPage -> {
                        val newState = state.value.onLoading(state.value.searchParams)
                        emit(newState)
                        useCase.search(newState.searchParams, newState.nextCursor).fold(
                            { emit(newState.onError()) },
                            { emit(newState.onSuccess(it)) }
                        )
                    }
                }
            }
        }
        .onEach { state.emit(it) }
        .onStart { emit(initialState) }
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

    sealed interface Event {
        fun isValid(state: State): Boolean

        object Retry : Event {
            override fun isValid(state: State) = state.error
        }

        data class NewSearch(val searchParams: SearchParams) : Event {
            override fun isValid(state: State) = true
        }

        object NextPage : Event {
            override fun isValid(state: State) = state.nextCursor != null && !state.loading && !state.error
        }
    }

    data class State(
        val searchParams: SearchParams,
        val nextCursor: String?,
        val error: Boolean,
        val loading: Boolean,
        val items: List<SearchResultItem>?,
    )

}
