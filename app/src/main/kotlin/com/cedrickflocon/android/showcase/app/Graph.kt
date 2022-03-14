package com.cedrickflocon.android.showcase.app

import com.cedrickflocon.android.showcase.BuildConfig
import com.cedrickflocon.android.showcase.user.di.DaggerUserComponent
import com.cedrickflocon.android.showcase.user.di.UserComponent
import com.cedrickflocon.showcase.di.DaggerDataComponent
import com.cedrickflocon.showcase.di.DataComponent

class Graph {

    private val dataComponent: DataComponent by lazy {
        DaggerDataComponent
            .factory()
            .create(BuildConfig.BEARER)
    }

    val userComponent: UserComponent by lazy {
        DaggerUserComponent
            .factory()
            .create(dataComponent)
    }

}
