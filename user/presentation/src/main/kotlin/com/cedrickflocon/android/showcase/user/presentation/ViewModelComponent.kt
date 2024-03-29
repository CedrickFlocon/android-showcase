package com.cedrickflocon.android.showcase.user.presentation

import com.cedrickflocon.android.showcase.user.di.UserComponent
import com.cedrickflocon.android.showcase.user.router.UserParams
import com.cedrickflocon.android.showcase.core.presentation.ViewModelScope
import dagger.BindsInstance
import dagger.Component

@ViewModelScope
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
