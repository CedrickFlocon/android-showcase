package com.cedrickflocon.android.showcase.user.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.cedrickflocon.android.showcase.design.Theme
import com.cedrickflocon.android.showcase.user.domain.UserUseCaseImpl

class UserActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Theme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val dataSource = remember { UserViewModel(UserUseCaseImpl(), "Me") }
                    val value = dataSource.data.collectAsState(initial = null).value

                    Column {
                        when (value) {
                            is UserViewModel.UiState.Error -> Text(value.error)
                            is UserViewModel.UiState.Success -> Text(value.name)
                            UserViewModel.UiState.Loading,
                            null -> CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }

}
