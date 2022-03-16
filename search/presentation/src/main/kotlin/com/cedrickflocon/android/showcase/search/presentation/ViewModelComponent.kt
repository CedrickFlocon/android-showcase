package com.cedrickflocon.android.showcase.search.presentation

import android.app.Activity
import com.cedrickflocon.android.showcase.search.di.SearchComponent
import dagger.BindsInstance
import dagger.Component

@Component(dependencies = [SearchComponent::class])
interface ViewModelComponent {

    fun provideViewModel(): SearchViewModel

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance activity: Activity,
            searchComponent: SearchComponent,
        ): ViewModelComponent
    }

}
