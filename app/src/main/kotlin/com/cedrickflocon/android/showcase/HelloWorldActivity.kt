package com.cedrickflocon.android.showcase

import android.app.Activity
import android.os.Bundle
import android.widget.TextView

class HelloWorldActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val textView = TextView(baseContext).apply {
            text = "Hello world"
        }
        setContentView(textView)
    }

}
