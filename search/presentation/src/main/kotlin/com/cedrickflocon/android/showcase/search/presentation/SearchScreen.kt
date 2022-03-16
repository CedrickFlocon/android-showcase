package com.cedrickflocon.android.showcase.search.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

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
                        is SearchViewModel.UiState.Success.Item.User -> Text(
                            text = item.login,
                            modifier = Modifier.clickable(onClick = item.onClickItem)
                        )
                    }
                }
            }
        }
        SearchViewModel.UiState.Loading, null -> Text("Loading")
    }
}
