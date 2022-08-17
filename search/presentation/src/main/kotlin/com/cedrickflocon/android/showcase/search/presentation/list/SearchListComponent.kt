package com.cedrickflocon.android.showcase.search.presentation.list

import android.app.Activity
import com.cedrickflocon.android.showcase.search.di.SearchComponent
import com.cedrickflocon.showcase.core.presentation.ViewModelScope
import dagger.BindsInstance
import dagger.Component
import kotlinx.coroutines.CoroutineScope

@ViewModelScope
@Component(dependencies = [SearchComponent::class])
interface SearchListComponent {

    fun provideViewModel(): SearchListViewModel

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance activity: Activity,
            @BindsInstance scope: CoroutineScope,
            searchComponent: SearchComponent
        ): SearchListComponent
    }

}
