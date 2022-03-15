package com.cedrickflocon.android.showcase.search.presentation

import com.cedrickflocon.android.showcase.search.domain.SearchUseCase
import kotlinx.coroutines.flow.flow
import java.net.URI
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val useCase: SearchUseCase,
    private val mapper: UiStateMapper
) {

    val data = flow {
        this.emit(UiState.Loading)
        useCase.search("Cedrick").fold(
            { this.emit(UiState.Error) },
            { this.emit(mapper(it)) }
        )
    }


    sealed interface UiState {
        object Loading : UiState
        object Error : UiState

        data class Success(
            val items: List<Item>
        ) : UiState {
            sealed interface Item {
                data class User(
                    val email: String,
                    val login: String,
                    val avatarUrl: URI
                ) : Item
            }
        }
    }

}
