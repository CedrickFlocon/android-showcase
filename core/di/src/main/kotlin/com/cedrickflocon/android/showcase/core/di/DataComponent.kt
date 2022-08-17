package com.cedrickflocon.android.showcase.core.di

import com.cedrickflocon.android.showcase.core.data.graphql.GraphQLClient
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component
interface DataComponent {

    fun gqlClient(): GraphQLClient

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance bearer: String
        ): DataComponent
    }

    interface Provider {
        fun provideDataComponent(): DataComponent
    }
}