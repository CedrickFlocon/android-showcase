package com.cedrickflocon.android.showcase.search.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.staticCompositionLocalOf
import com.cedrickflocon.android.showcase.core.presentation.design.Theme
import com.cedrickflocon.android.showcase.search.di.DaggerSearchComponent
import com.cedrickflocon.android.showcase.search.di.SearchComponent
import com.cedrickflocon.showcase.core.di.DataComponent

val LocalSearchComponent = staticCompositionLocalOf<SearchComponent> {
    error("CompositionLocal SearchComponent not present")
}

class SearchActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Theme {
                val scope = rememberCoroutineScope()
                val searchComponent = remember {
                    DaggerSearchComponent
                        .factory()
                        .create(
                            scope,
                            (application as DataComponent.Provider).provideDataComponent()
                        )
                }
                CompositionLocalProvider(LocalSearchComponent provides searchComponent) {
                    SearchScreen()
                }
            }
        }
    }
}
