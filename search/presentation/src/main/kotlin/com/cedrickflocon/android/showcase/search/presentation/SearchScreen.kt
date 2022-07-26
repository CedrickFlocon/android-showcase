package com.cedrickflocon.android.showcase.search.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cedrickflocon.android.showcase.search.presentation.field.SearchTextField
import com.cedrickflocon.android.showcase.search.presentation.list.SearchList

@Composable
fun SearchScreen() {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            SearchTextField()
            SearchList()
        }
    }
}
