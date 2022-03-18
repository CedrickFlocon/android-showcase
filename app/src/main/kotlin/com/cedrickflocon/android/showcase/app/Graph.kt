package com.cedrickflocon.android.showcase.app

import com.cedrickflocon.android.showcase.BuildConfig
import com.cedrickflocon.android.showcase.search.di.DaggerSearchComponent
import com.cedrickflocon.android.showcase.search.di.SearchComponent
import com.cedrickflocon.android.showcase.user.di.DaggerUserComponent
import com.cedrickflocon.android.showcase.user.di.UserComponent
import com.cedrickflocon.showcase.core.di.DaggerDataComponent
import com.cedrickflocon.showcase.core.di.DataComponent

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

    val searchComponent: SearchComponent by lazy {
        DaggerSearchComponent
            .factory()
            .create(dataComponent)
    }

}
