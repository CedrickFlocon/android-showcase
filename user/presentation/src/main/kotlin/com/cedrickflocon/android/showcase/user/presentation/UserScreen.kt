package com.cedrickflocon.android.showcase.user.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun UserScreen(state: UserViewModel.UiState?) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column {
            when (state) {
                is UserViewModel.UiState.Error -> Text(state.error)
                is UserViewModel.UiState.Success -> Text(state.name)
                UserViewModel.UiState.Loading,
                null -> CircularProgressIndicator()
            }
        }
    }
}