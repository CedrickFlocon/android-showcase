package com.cedrickflocon.android.showcase.user.presentation

import com.cedrickflocon.android.showcase.user.di.UserComponent
import com.cedrickflocon.android.showcase.user.router.UserParams
import dagger.BindsInstance
import dagger.Component

@Component(dependencies = [UserComponent::class])
interface ViewModelComponent {

    fun provideViewModel(): UserViewModel

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance userParams: UserParams,
            userComponent: UserComponent,
        ): ViewModelComponent
    }

}
