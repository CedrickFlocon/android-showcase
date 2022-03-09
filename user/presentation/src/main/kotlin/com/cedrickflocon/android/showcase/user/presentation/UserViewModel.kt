package com.cedrickflocon.android.showcase.user.presentation

import com.cedrickflocon.android.showcase.user.domain.UserUseCase
import kotlinx.coroutines.flow.flow
import java.net.URI
import javax.inject.Inject


class UserViewModel @Inject constructor(
    private val userParams: UserParams,
    private val useCase: UserUseCase,
    private val mapper: UiStateMapper,
) {

    val data = flow {
        this.emit(UiState.Loading)
        useCase.getUser(userParams.login).fold(
            { this.emit(UiState.Error("error")) },
            { this.emit(mapper(it)) },
        )
    }

    sealed interface UiState {
        object Loading : UiState
        data class Error(val error: String) : UiState
        data class Success(val login: String, val avatarUrl: URI) : UiState
    }

}
