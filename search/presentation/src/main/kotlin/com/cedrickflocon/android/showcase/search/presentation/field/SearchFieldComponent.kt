package com.cedrickflocon.android.showcase.search.presentation.field

import com.cedrickflocon.android.showcase.search.di.SearchComponent
import com.cedrickflocon.android.showcase.search.presentation.ViewModelScope
import dagger.BindsInstance
import dagger.Component
import kotlinx.coroutines.CoroutineScope

@ViewModelScope
@Component(dependencies = [SearchComponent::class])
interface SearchFieldComponent {

    fun provideViewModel(): SearchFieldViewModel

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance scope: CoroutineScope,
            searchComponent: SearchComponent
        ): SearchFieldComponent
    }

}
