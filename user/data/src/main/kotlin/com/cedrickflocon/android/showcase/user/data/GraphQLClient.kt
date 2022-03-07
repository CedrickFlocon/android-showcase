package com.cedrickflocon.android.showcase.user.data

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.http.LoggingInterceptor
import com.apollographql.apollo3.network.okHttpClient
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import javax.inject.Inject

class GraphQLClient @Inject constructor(
    private val bearer: String
) {

    val client = ApolloClient.Builder()
        .serverUrl("https://api.github.com/graphql")
        .addHttpInterceptor(LoggingInterceptor())
        .okHttpClient(
            OkHttpClient.Builder()
                .addInterceptor(AuthorizationInterceptor(bearer))
                .build()
        )
        .build()

    private class AuthorizationInterceptor(
        private val bearer: String
    ) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request().newBuilder()
                .addHeader("Authorization", "bearer $bearer")
                .build()

            return chain.proceed(request)
        }
    }

}
