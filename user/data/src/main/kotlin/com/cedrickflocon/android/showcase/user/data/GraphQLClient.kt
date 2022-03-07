package com.cedrickflocon.android.showcase.user.data

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.http.LoggingInterceptor
import com.apollographql.apollo3.network.okHttpClient
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import javax.inject.Inject

class GraphQLClient @Inject constructor() {

    val client = ApolloClient.Builder()
        .serverUrl("https://api.github.com/graphql")
        .build()

}
