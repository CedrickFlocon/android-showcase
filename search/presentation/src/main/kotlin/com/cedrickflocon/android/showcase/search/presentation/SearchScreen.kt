package com.cedrickflocon.android.showcase.search.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

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
                items(uiState.items.size) { index ->
                    val item = uiState.items[index]
                    when (item) {
                        is SearchViewModel.UiState.Success.Item.User -> Text(item.login)
                    }
                }
            }
        }
        SearchViewModel.UiState.Loading, null -> Text("Loading")
    }
}
