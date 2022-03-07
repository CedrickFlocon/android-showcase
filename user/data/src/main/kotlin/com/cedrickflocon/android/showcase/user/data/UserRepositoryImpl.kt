package com.cedrickflocon.android.showcase.user.data

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.apollographql.apollo3.exception.ApolloException
import com.cedrickflocon.android.showcase.user.domain.User
import com.cedrickflocon.android.showcase.user.domain.UserError
import com.cedrickflocon.android.showcase.user.domain.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val graphQLClient: GraphQLClient
) : UserRepository {

    override suspend fun getUser(login: String): Either<UserError, User> {
        val response = try {
            graphQLClient.client.query(GetUserQuery(login)).execute()
        } catch (e: ApolloException) {
            return UserError.Network.left()
        }
        return response.dataAssertNoErrors.user.let { User(it.name, it.avatarUrl) }.right()
    }

}
