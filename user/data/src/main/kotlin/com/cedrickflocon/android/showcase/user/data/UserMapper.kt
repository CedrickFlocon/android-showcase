package com.cedrickflocon.android.showcase.user.data

import com.cedrickflocon.android.showcase.user.domain.User
import javax.inject.Inject

class UserMapper @Inject constructor() : (GetUserQuery.User) -> User {

    override fun invoke(gqlUser: GetUserQuery.User): User {
        return User(gqlUser.login, gqlUser.avatarUrl)
    }

}
