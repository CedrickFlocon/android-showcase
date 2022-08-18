package com.cedrickflocon.android.showcase.search.presentation.list

import com.cedrickflocon.android.showcase.search.domain.SearchResultItem
import com.cedrickflocon.android.showcase.search.domain.SearchUseCase
import com.cedrickflocon.android.showcase.user.router.UserParams
import com.cedrickflocon.android.showcase.user.router.UserRouter
import kotlinx.coroutines.flow.*
import java.net.URI
import javax.inject.Inject
import kotlin.random.Random
import kotlin.random.nextInt

class SearchListViewModel @Inject constructor(
    private val useCase: SearchUseCase,
    private val mapper: UiStateMapper,
    private val router: UserRouter
) {

    val initialState = UiState(
        items = null,
        error = null,
    ) { events.emit(Event.OnScroll(it)) }

    private val loadingItems = List(3) {
        UiState.Item(
            loading = true,
            email = (0..Random.nextInt(10 until 20)).joinToString("") { " " },
            login = (0..Random.nextInt(10 until 20)).joinToString("") { " " },
            avatarUrl = URI(""),
            onClickItem = {})
    }
    private val events = MutableSharedFlow<Event>()

    val states = merge(
        useCase.search
            .map { state ->
                val result = state.items
                    ?.let { mapper(it) { events.emit(Event.OnClickUser(it)) } }

                val loading = loadingItems
                    .takeIf { state.loading }

                initialState.copy(
                    error = if (state.error) ({ useCase.events.emit(SearchUseCase.Event.Retry) }) else null,
                    items = (result?.plus(loading ?: emptyList()) ?: loading)
                )
            }
            .onStart { emit(initialState) }
            .distinctUntilChanged(),

        events
            .combine(useCase.search) { event, data -> event to data }
            .onEach { (event, data) ->
                when (event) {
                    is Event.OnScroll -> if (event.index >= (data.items?.size?.minus(1) ?: Int.MAX_VALUE)) {
                        useCase.events.emit(SearchUseCase.Event.NextPage)
                    }
                    is Event.OnClickUser -> router.navigateToUser(UserParams(event.searchResultItem.login))
                }
            }
            .flatMapLatest { emptyFlow() }
    )

    private sealed interface Event {
        data class OnClickUser(val searchResultItem: SearchResultItem.User) : Event
        data class OnScroll(val index: Int) : Event
    }

    data class UiState(
        val items: List<Item>?,
        val error: (suspend () -> Unit)?,
        val onScroll: suspend (Int) -> Unit
    ) {
        data class Item(
            val loading: Boolean,
            val email: String,
            val login: String,
            val avatarUrl: URI,
            val onClickItem: suspend () -> Unit,
        )
    }

}
