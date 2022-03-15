package com.cedrickflocon.android.showcase.search.di

import com.cedrickflocon.android.showcase.search.data.SearchRepositoryImpl
import com.cedrickflocon.android.showcase.search.domain.SearchRepository
import com.cedrickflocon.android.showcase.search.domain.SearchUseCase
import com.cedrickflocon.android.showcase.search.domain.SearchUseCaseImpl
import com.cedrickflocon.showcase.core.di.DataComponent
import dagger.Binds
import dagger.Component
import dagger.Module

@Component(
    dependencies = [DataComponent::class],
    modules = [SearchComponent.SearchModule::class]
)
interface SearchComponent {

    fun useCase(): SearchUseCase

    @Component.Factory
    interface Factory {
        fun create(
            dataComponent: DataComponent
        ): SearchComponent
    }

    @Module
    interface SearchModule {

        @Binds
        fun bindUseCase(useCase: SearchUseCaseImpl): SearchUseCase

        @Binds
        fun bindRepository(repository: SearchRepositoryImpl): SearchRepository

    }

    interface Provider {
        fun provideSearchComponent(): SearchComponent
    }

}
