package com.cedrickflocon.showcase.core.di

import com.cedrickflocon.android.showcase.core.data.graphql.GraphQLClient
import dagger.BindsInstance
import dagger.Component

@Component
interface DataComponent {

    fun gqlClient(): GraphQLClient

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance bearer: String
        ): DataComponent
    }

}