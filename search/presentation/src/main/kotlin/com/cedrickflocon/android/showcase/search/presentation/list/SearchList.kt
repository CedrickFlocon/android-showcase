package com.cedrickflocon.android.showcase.search.presentation.list

import android.app.Activity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.cedrickflocon.android.showcase.search.presentation.LocalSearchComponent
import com.cedrickflocon.android.showcase.search.presentation.R
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch

@Composable
fun SearchList() {
    val coroutineScope = rememberCoroutineScope()
    val searchComponent = LocalSearchComponent.current
    val activity = LocalContext.current as Activity
    val viewModel = remember {
        DaggerSearchListComponent
            .factory()
            .create(
                activity,
                coroutineScope,
                searchComponent,
            )
            .provideViewModel()
    }

    val uiState = viewModel.states.collectAsState(initial = viewModel.initialState).value

    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
    ) {
        it.calculateBottomPadding()
        if (uiState.items?.isEmpty() == true) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = stringResource(R.string.showcase_search_empty))
            }
        } else if (uiState.items != null) {
            val listState = rememberLazyListState()
            LazyColumn(
                state = listState,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                itemsIndexed(uiState.items) { _, item -> UserItem(item) }
            }

            LaunchedEffect(listState) {
                snapshotFlow { listState.layoutInfo.visibleItemsInfo }
                    .distinctUntilChanged()
                    .mapNotNull { it.lastOrNull()?.index }
                    .collect { uiState.onScroll(it) }
            }

            val errorString = stringResource(id = R.string.showcase_search_error)
            val retryString = stringResource(id = R.string.showcase_search_retry)

            LaunchedEffect(uiState.error) {
                if (uiState.error != null) {
                    val snackbarResult = scaffoldState.snackbarHostState.showSnackbar(errorString, retryString, SnackbarDuration.Indefinite)
                    when (snackbarResult) {
                        SnackbarResult.ActionPerformed -> uiState.error.invoke()
                    }
                }
            }
        } else if (uiState.error != null) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(stringResource(id = R.string.showcase_search_error))
                Button(onClick = { coroutineScope.launch { uiState.error.invoke() } }) {
                    Text(stringResource(id = R.string.showcase_search_retry))
                }
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = stringResource(R.string.showcase_search_clean))
            }
        }
    }
}

@Composable
private fun Modifier.loading(user: SearchListViewModel.UiState.Item): Modifier {
    return this.placeholder(
        visible = user.loading,
        highlight = PlaceholderHighlight.shimmer()
    )
}

@Composable
fun UserItem(user: SearchListViewModel.UiState.Item) {
    val scope = rememberCoroutineScope()
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .clickable(onClick = { scope.launch { user.onClickItem() } })
    ) {
        AsyncImage(
            model = user.avatarUrl.toString(),
            contentDescription = null,
            modifier = Modifier
                .width(50.dp)
                .height(50.dp)
                .clip(CircleShape)
                .loading(user)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = user.login,
                modifier = Modifier.loading(user),
                style = MaterialTheme.typography.body1,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = user.email,
                modifier = Modifier
                    .padding(top = 4.dp)
                    .loading(user),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}