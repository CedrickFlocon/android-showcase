package com.cedrickflocon.android.showcase.user.presentation

import com.cedrickflocon.android.showcase.user.domain.UserUseCase
import kotlinx.coroutines.flow.flow
import java.net.URL


class UserViewModel(
    private val useCase: UserUseCase,
    private val login: String
) {

    val data = flow {
        this.emit(UiState.Loading)
        useCase.getUser(login).fold(
            { this.emit(UiState.Error("error")) },
            { this.emit(UiState.Success(it.name, it.avatarUrl)) },
        )
    }

    sealed interface UiState {
        object Loading : UiState
        data class Error(val error: String) : UiState
        data class Success(val name: String, val avatarUrl: URL) : UiState
    }

}