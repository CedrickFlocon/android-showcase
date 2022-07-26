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

    val events = MutableSharedFlow<Event>()
    val states = events
        .flatMapLatest {
            flow {
                when (it) {
                    is Event.NewSearch -> {
                        emit(State.Loading)
                        useCase.search(it.searchParams, null).fold(
                            { emit(State.Error) },
                            { emit(State.Result(it)) }
                        )
                    }
                }
            }
        }
        .distinctUntilChanged()
        .onStart { emit(State.Clean) }
        .shareIn(externalScope, SharingStarted.Eagerly, 1)

    sealed interface Event {
        data class NewSearch(val searchParams: SearchParams) : Event
    }

    sealed interface State {
        object Clean : State

        object Loading : State

        object Error : State

        data class Result(val searchResult: SearchResult) : State
    }

}
