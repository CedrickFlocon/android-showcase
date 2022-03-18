package com.cedrickflocon.android.showcase.search.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.TextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

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
            itemsIndexed(uiState.items) { _, item ->
                when (item) {
                    is SearchViewModel.UiState.Item.User -> UserItem(item)
                }
            }
        }
    } else if (uiState.error) {
        Text("Error")
    } else if (uiState.loading) {
        Text("Loading")
    }
}

@Composable
fun UserItem(user: SearchViewModel.UiState.Item.User) {
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
