package com.cedrickflocon.android.showcase.user.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun UserScreen(state: UserViewModel.UiState?) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        when (state) {
            is UserViewModel.UiState.Error -> Error(state)
            is UserViewModel.UiState.Success -> UserProfile(state)
            UserViewModel.UiState.Loading,
            null -> Loading()
        }
    }
}

@Composable
private fun UserProfile(state: UserViewModel.UiState.Success) {
    Row(modifier = Modifier.fillMaxSize()) {
        AsyncImage(
            model = state.avatarUrl.toString(),
            contentDescription = null,
            modifier = Modifier
                .width(50.dp)
                .height(50.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(state.login)
    }
}

@Composable
private fun Loading() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(modifier = Modifier.size(50.dp))
    }
}

@Composable
private fun Error(state: UserViewModel.UiState.Error) {
    Text(state.error)
}
