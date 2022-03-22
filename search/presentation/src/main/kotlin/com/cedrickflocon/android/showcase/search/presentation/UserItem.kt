package com.cedrickflocon.android.showcase.search.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer

@Composable
private fun Modifier.loading(user: SearchViewModel.UiState.Item): Modifier {
    return this.placeholder(
        visible = user.loading,
        highlight = PlaceholderHighlight.shimmer()
    )
}

@Composable
fun UserItem(user: SearchViewModel.UiState.Item) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .clickable(onClick = user.onClickItem)
    ) {
        AsyncImage(
            model = user.avatarUrl.toString(),
            contentDescription = null,
            modifier = Modifier
                .loading(user)
                .width(50.dp)
                .height(50.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = user.login,
                modifier = Modifier.loading(user),
                style = MaterialTheme.typography.headlineMedium,
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
