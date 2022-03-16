package com.cedrickflocon.android.showcase.search.presentation

import com.cedrickflocon.android.showcase.search.domain.SearchUseCase
import com.cedrickflocon.android.showcase.user.router.UserParams
import com.cedrickflocon.android.showcase.user.router.UserRouter
import kotlinx.coroutines.flow.flow
import java.net.URI
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val useCase: SearchUseCase,
    private val mapper: UiStateMapper,
    private val router: UserRouter,
) {

    val data = flow {
        this.emit(UiState.Loading)
        useCase.search("Cedrick").fold(
            { this.emit(UiState.Error) },
            { this.emit(mapper(it) { login -> router.navigateToUser(UserParams(login)) }) }
        )
    }


    sealed interface UiState {
        object Loading : UiState
        object Error : UiState

        data class Success(
            val items: List<Item>,
        ) : UiState {
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

}
