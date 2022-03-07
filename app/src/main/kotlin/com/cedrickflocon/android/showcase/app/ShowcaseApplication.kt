package com.cedrickflocon.android.showcase.app

import android.app.Application
import com.cedrickflocon.android.showcase.user.di.DaggerUserComponent
import com.cedrickflocon.android.showcase.user.di.UserComponent

class ShowcaseApplication :
    Application(),
    UserComponent.Provider {

    private val userComponent: UserComponent by lazy { DaggerUserComponent.factory().create() }

    override fun provideComponent(): UserComponent = userComponent

}
