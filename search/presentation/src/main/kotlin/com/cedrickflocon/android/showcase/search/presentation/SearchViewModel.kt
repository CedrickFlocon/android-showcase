package com.cedrickflocon.android.showcase.search.presentation

import com.cedrickflocon.android.showcase.search.domain.SearchUseCase
import com.cedrickflocon.android.showcase.user.router.UserParams
import com.cedrickflocon.android.showcase.user.router.UserRouter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import java.net.URI
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModel @Inject constructor(
    private val useCase: SearchUseCase,
    private val mapper: UiStateMapper,
    private val router: UserRouter,
) {

    val initialState = UiState(
        loading = false,
        error = false,
        items = null
    ) { events.tryEmit(Event.OnSearch(it)) }

    private val events = MutableSharedFlow<Event>(extraBufferCapacity = 1)
    val states = events
        .onStart { emit(Event.OnOpen) }
        .flatMapLatest {
            flow {
                when (it) {
                    Event.OnOpen -> emit(initialState)
                    is Event.OnSearch -> {
                        emit(initialState.onLoading())
                        useCase.search(it.query).fold(
                            { emit(initialState.onError()) },
                            {
                                emit(
                                    initialState.onSuccess(mapper(it) { login ->
                                        router.navigateToUser(UserParams(login))
                                    })
                                )
                            }
                        )
                    }
                }
            }
        }

    private fun UiState.onLoading() = this.copy(
        loading = true,
        error = false
    )

    private fun UiState.onSuccess(items: List<UiState.Item>) = this.copy(
        loading = false,
        error = false,
        items = items
    )

    private fun UiState.onError() = this.copy(
        loading = false,
        error = true,
    )

    private sealed interface Event {
        object OnOpen : Event
        data class OnSearch(val query: String) : Event
    }

    data class UiState(
        val loading: Boolean,
        val error: Boolean,
        val items: List<Item>?,
        val onSearch: (String) -> Unit
    ) {
        sealed interface Item {
            data class User(
                val email: String,
                val login: String,
                val avatarUrl: URI,
                val onClickItem: () -> Unit,
            ) : Item
        }
    }

}
