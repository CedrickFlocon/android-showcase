package com.cedrickflocon.android.showcase.user.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import com.cedrickflocon.android.showcase.design.Theme

class UserActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Theme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Text(text = "Hello World")
                }
            }
        }
    }

}
