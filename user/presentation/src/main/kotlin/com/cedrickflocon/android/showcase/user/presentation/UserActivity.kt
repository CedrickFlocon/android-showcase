package com.cedrickflocon.android.showcase.user.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.cedrickflocon.android.showcase.design.Theme
import com.cedrickflocon.android.showcase.user.di.UserComponent

class UserActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Theme {
                val viewModel = remember {
                    DaggerViewModelComponent
                        .factory()
                        .create(
                            UserParams("CedrickFlocon"),
                            (application as UserComponent.Provider).provideUserComponent()
                        )
                        .provideViewModel()
                }
                val value = viewModel.data.collectAsState(initial = null).value
                Surface(modifier = Modifier.fillMaxSize()) {
                    UserScreen(value)
                }
            }
        }
    }
}
