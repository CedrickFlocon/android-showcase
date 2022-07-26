package com.cedrickflocon.android.showcase.user.di

import com.cedrickflocon.android.showcase.user.data.UserRepositoryImpl
import com.cedrickflocon.android.showcase.user.domain.UserRepository
import com.cedrickflocon.android.showcase.user.domain.UserUseCase
import com.cedrickflocon.android.showcase.user.domain.UserUseCaseImpl
import com.cedrickflocon.showcase.core.di.DataComponent
import dagger.Binds
import dagger.Component
import dagger.Module

@Component(
    dependencies = [DataComponent::class],
    modules = [UserComponent.UserModule::class]
)
interface UserComponent {

    fun useCase(): UserUseCase

    @Component.Factory
    interface Factory {
        fun create(
            dataComponent: DataComponent
        ): UserComponent
    }

    @Module
    interface UserModule {

        @Binds
        fun bindUseCase(useCase: UserUseCaseImpl): UserUseCase

        @Binds
        fun bindRepository(repository: UserRepositoryImpl): UserRepository

    }

}
