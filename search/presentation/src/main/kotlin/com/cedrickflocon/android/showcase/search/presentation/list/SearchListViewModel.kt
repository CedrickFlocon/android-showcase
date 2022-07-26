package com.cedrickflocon.android.showcase.search.presentation.list

import com.cedrickflocon.android.showcase.search.domain.SearchDataSource
import com.cedrickflocon.android.showcase.user.router.UserParams
import com.cedrickflocon.android.showcase.user.router.UserRouter
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import java.net.URI
import javax.inject.Inject
import kotlin.random.Random
import kotlin.random.nextInt

class SearchListViewModel @Inject constructor(
    private val dataSource: SearchDataSource,
    private val mapper: UiStateMapper,
    private val router: UserRouter
) {

    val initialState = UiState(
        items = null,
        error = false,
    )

    val states = dataSource.states
        .map {
            when (it) {
                SearchDataSource.State.Clean -> initialState
                SearchDataSource.State.Error -> initialState.onError()
                SearchDataSource.State.Loading -> initialState.onLoading()
                is SearchDataSource.State.Result -> initialState.onSuccess(mapper(it.searchResult) {
                    router.navigateToUser(UserParams(it))
                })
            }
        }
        .onStart { emit(initialState) }
        .distinctUntilChanged()

    private fun UiState.onLoading() = this.copy(
        items = List(3) {
            UiState.Item(
                loading = true,
                email = (0..Random.nextInt(10 until 20)).joinToString("") { " " },
                login = (0..Random.nextInt(10 until 20)).joinToString("") { " " },
                avatarUrl = URI(""),
                onClickItem = {})
        },
        error = false
    )

    private fun UiState.onSuccess(items: List<UiState.Item>) = this.copy(
        items = items,
        error = false
    )

    private fun UiState.onError() = this.copy(
        items = null,
        error = true,
    )

    data class UiState(
        val items: List<Item>?,
        val error: Boolean,
    ) {
        data class Item(
            val loading: Boolean,
            val email: String,
            val login: String,
            val avatarUrl: URI,
            val onClickItem: () -> Unit,
        )
    }

}
