package com.cedrickflocon.android.showcase.search.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.TextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction

@Composable
fun SearchScreen(uiState: SearchViewModel.UiState) {
    Column(modifier = Modifier.fillMaxSize()) {
        SearchTextField(uiState)
        UserSearchResultList(uiState)
    }
}

@Composable
fun SearchTextField(uiState: SearchViewModel.UiState) {
    var text by rememberSaveable { mutableStateOf("") }
    TextField(
        value = text,
        onValueChange = { text = it },
        label = { Text(text = stringResource(id = R.string.showcase_search_hint)) },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { uiState.onSearch(text) }),
        maxLines = 1,
    )
}

@Composable
fun UserSearchResultList(uiState: SearchViewModel.UiState) {
    if (uiState.items?.isEmpty() == true) {
        Text("Empty")
    } else if (uiState.items != null) {
        LazyColumn {
            itemsIndexed(uiState.items) { _, item -> UserItem(item) }
        }
    } else if (uiState.error) {
        Text("Error")
    }
}
