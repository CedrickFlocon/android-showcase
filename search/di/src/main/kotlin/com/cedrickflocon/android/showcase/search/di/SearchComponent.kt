package com.cedrickflocon.android.showcase.search.di

import com.cedrickflocon.android.showcase.search.data.SearchRepositoryImpl
import com.cedrickflocon.android.showcase.search.domain.SearchDataSource
import com.cedrickflocon.android.showcase.search.domain.SearchRepository
import com.cedrickflocon.android.showcase.search.domain.SearchUseCase
import com.cedrickflocon.android.showcase.search.domain.SearchUseCaseImpl
import com.cedrickflocon.showcase.core.di.DataComponent
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [DataComponent::class],
    modules = [SearchComponent.SearchModule::class]
)
interface SearchComponent {

    fun searchDataSource(): SearchDataSource

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance scope: CoroutineScope,
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

}
