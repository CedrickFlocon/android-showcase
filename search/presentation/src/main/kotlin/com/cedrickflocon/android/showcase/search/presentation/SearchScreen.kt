package com.cedrickflocon.android.showcase.search.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

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
        placeholder = { Text(text = stringResource(id = R.string.showcase_search_hint)) },
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, top = 8.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(25.dp)),
        leadingIcon = { Icon(Icons.Filled.Search, null, tint = Color.Gray) },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { uiState.onSearch(text) }),
        maxLines = 1,
    )
}

@Composable
fun UserSearchResultList(uiState: SearchViewModel.UiState) {
    if (uiState.items?.isEmpty() == true) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = stringResource(R.string.showcase_search_empty))
        }

    } else if (uiState.items != null) {
        LazyColumn(modifier = Modifier.padding(top = 8.dp)) {
            itemsIndexed(uiState.items) { _, item -> UserItem(item) }
        }
    } else if (uiState.error) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(stringResource(id = R.string.showcase_search_error))
        }
    }
}
