package com.cedrickflocon.android.showcase.search.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

interface SearchUseCase {

    val events: MutableSharedFlow<Event>
    val data: Flow<State>

    sealed interface Event {
        object Retry : Event
        data class NewSearch(val searchParams: SearchParams) : Event
        object NextPage : Event
    }

    data class State(
        val searchParams: SearchParams,
        val nextCursor: String?,
        val error: Boolean,
        val loading: Boolean,
        val items: List<SearchResultItem>?,
    )

}
