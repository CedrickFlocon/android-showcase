package com.cedrickflocon.android.showcase.user.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.cedrickflocon.android.showcase.core.presentation.design.Theme
import com.cedrickflocon.android.showcase.user.di.DaggerUserComponent
import com.cedrickflocon.android.showcase.user.di.UserComponent
import com.cedrickflocon.android.showcase.user.router.UserRouter
import com.cedrickflocon.android.showcase.core.di.DataComponent

class UserActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Theme {
                val viewModel = remember {
                    val userComponent: UserComponent = DaggerUserComponent
                        .factory()
                        .create((application as DataComponent.Provider).provideDataComponent())

                    DaggerViewModelComponent
                        .factory()
                        .create(
                            UserRouter.readUserIntent(intent),
                            userComponent
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
