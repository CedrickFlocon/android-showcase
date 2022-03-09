package com.cedrickflocon.android.showcase.user.data

import com.cedrickflocon.android.showcase.user.domain.User
import javax.inject.Inject

class UserMapper @Inject constructor() : (GetUserQuery.User) -> User {

    override fun invoke(gqlUser: GetUserQuery.User): User {
        return User(
            name = gqlUser.name,
            login = gqlUser.login,
            avatarUrl = gqlUser.avatarUrl,
            company = gqlUser.company,
            location = gqlUser.location,
            isHireable = gqlUser.isHireable,
            followersCount = gqlUser.followers.totalCount,
            followingCount = gqlUser.following.totalCount,
        )
    }

}
