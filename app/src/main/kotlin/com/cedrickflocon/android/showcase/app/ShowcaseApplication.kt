package com.cedrickflocon.android.showcase.app

import android.app.Application
import com.cedrickflocon.android.showcase.BuildConfig
import com.cedrickflocon.android.showcase.user.di.DaggerUserComponent
import com.cedrickflocon.android.showcase.user.di.UserComponent

class ShowcaseApplication :
    Application(),
    UserComponent.Provider {

    private val graph = Graph()

    override fun provideComponent(): UserComponent = graph.userComponent

}
