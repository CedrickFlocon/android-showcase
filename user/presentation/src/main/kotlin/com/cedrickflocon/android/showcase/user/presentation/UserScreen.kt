package com.cedrickflocon.android.showcase.user.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
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
    Column(modifier = Modifier.fillMaxSize()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = state.avatarUrl.toString(),
                contentDescription = null,
                modifier = Modifier
                    .width(50.dp)
                    .height(50.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                state.name?.let { Text(it, style = MaterialTheme.typography.headlineMedium) }
                Text(state.login)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            Icon(imageVector = Icons.Outlined.Home, contentDescription = null)
            state.company?.let { Text(state.company) }
            Icon(imageVector = Icons.Outlined.LocationOn, contentDescription = null)
            state.location?.let { Text(state.location) }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row {
            Icon(imageVector = Icons.Outlined.Person, contentDescription = null)
            Text(
                stringResource(
                    id = R.string.showcase_user_social_count,
                    state.followersCount, state.followingCount
                )
            )
        }

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
