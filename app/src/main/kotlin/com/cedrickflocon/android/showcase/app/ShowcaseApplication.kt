package com.cedrickflocon.android.showcase.app

import android.app.Application
import com.cedrickflocon.showcase.core.di.DataComponent

class ShowcaseApplication :
    Application(),
    DataComponent.Provider {

    private val graph = Graph()

    override fun provideDataComponent(): DataComponent = graph.dataComponent

}
