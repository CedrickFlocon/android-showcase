package com.cedrickflocon.android.showcase.app

import com.cedrickflocon.android.showcase.BuildConfig
import com.cedrickflocon.showcase.core.di.DaggerDataComponent
import com.cedrickflocon.showcase.core.di.DataComponent

class Graph {

    val dataComponent: DataComponent by lazy {
        DaggerDataComponent
            .factory()
            .create(BuildConfig.BEARER)
    }

}
