package com.cedrickflocon.android.showcase.search.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.cedrickflocon.android.showcase.design.Theme
import com.cedrickflocon.android.showcase.search.di.SearchComponent

class SearchActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Theme {
                val viewModel = remember {
                    DaggerViewModelComponent
                        .factory()
                        .create(
                            (application as SearchComponent.Provider).provideSearchComponent()
                        )
                        .provideViewModel()
                }

                val value = viewModel.data.collectAsState(initial = null).value

                Surface(modifier = Modifier.fillMaxSize()) {
                    SearchScreen(value)
                }
            }
        }
    }

}
