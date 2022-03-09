package com.cedrickflocon.android.showcase.user.di

import com.cedrickflocon.android.showcase.user.data.UserRepositoryImpl
import com.cedrickflocon.android.showcase.user.domain.UserRepository
import com.cedrickflocon.android.showcase.user.domain.UserUseCase
import com.cedrickflocon.android.showcase.user.domain.UserUseCaseImpl
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module

@Component(modules = [UserComponent.UserModule::class])
interface UserComponent {

    fun useCase(): UserUseCase

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance bearer: String
        ): UserComponent
    }

    @Module
    interface UserModule {

        @Binds
        fun bindUseCase(useCase: UserUseCaseImpl): UserUseCase

        @Binds
        fun bindRepository(repository: UserRepositoryImpl): UserRepository

    }

    interface Provider {
        fun provideComponent(): UserComponent
    }

}
