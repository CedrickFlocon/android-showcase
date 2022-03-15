package com.cedrickflocon.android.showcase.search.presentation

import com.cedrickflocon.android.showcase.search.di.SearchComponent
import dagger.Component

@Component(dependencies = [SearchComponent::class])
interface ViewModelComponent {

    fun provideViewModel(): SearchViewModel

    @Component.Factory
    interface Factory {
        fun create(
            searchComponent: SearchComponent,
        ): ViewModelComponent
    }

}
