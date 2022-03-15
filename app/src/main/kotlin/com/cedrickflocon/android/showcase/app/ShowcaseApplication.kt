package com.cedrickflocon.android.showcase.app

import android.app.Application
import com.cedrickflocon.android.showcase.search.di.SearchComponent
import com.cedrickflocon.android.showcase.user.di.UserComponent

class ShowcaseApplication :
    Application(),
    UserComponent.Provider,
    SearchComponent.Provider {

    private val graph = Graph()

    override fun provideUserComponent(): UserComponent = graph.userComponent

    override fun provideSearchComponent(): SearchComponent = graph.searchComponent

}
