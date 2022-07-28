package com.cedrickflocon.android.showcase.search.presentation.field

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.cedrickflocon.android.showcase.search.presentation.LocalSearchComponent
import com.cedrickflocon.android.showcase.search.presentation.R
import kotlinx.coroutines.launch

@Composable
fun SearchTextField() {
    var text by rememberSaveable { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val searchComponent = LocalSearchComponent.current
    val viewModel = remember {
        DaggerSearchFieldComponent
            .factory()
            .create(
                coroutineScope,
                searchComponent,
            )
            .provideViewModel()
    }

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
        keyboardActions = KeyboardActions(onSearch = { coroutineScope.launch { viewModel.search(text) } }),
        maxLines = 1,
    )
}