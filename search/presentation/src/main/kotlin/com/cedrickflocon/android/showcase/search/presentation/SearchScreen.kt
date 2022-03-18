package com.cedrickflocon.android.showcase.search.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun SearchScreen(uiState: SearchViewModel.UiState?) {
    Column {
        SearchTextField()
        UserSearchResultList(uiState)
    }
}


@Composable
fun SearchTextField() {

}


@Composable
fun UserSearchResultList(uiState: SearchViewModel.UiState?) {
    when (uiState) {
        SearchViewModel.UiState.Error -> Text("Error")
        is SearchViewModel.UiState.Success -> {
            LazyColumn {
                itemsIndexed(uiState.items) { _, item ->
                    when (item) {
                        is SearchViewModel.UiState.Success.Item.User -> UserItem(item)
                    }
                }
            }
        }
        SearchViewModel.UiState.Loading, null -> Text("Loading")
    }
}

@Composable
fun UserItem(user: SearchViewModel.UiState.Success.Item.User) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxSize()
            .clickable(onClick = user.onClickItem)
    ) {
        AsyncImage(
            model = user.avatarUrl.toString(),
            contentDescription = null,
            modifier = Modifier
                .width(50.dp)
                .height(50.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(user.login, style = MaterialTheme.typography.headlineMedium)
            Text(user.email)
        }
    }
}
