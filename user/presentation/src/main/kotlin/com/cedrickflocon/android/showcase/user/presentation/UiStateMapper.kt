package com.cedrickflocon.android.showcase.user.presentation

import com.cedrickflocon.android.showcase.user.domain.User
import javax.inject.Inject

class UiStateMapper @Inject constructor() : (User) -> UserViewModel.UiState.Success {

    override fun invoke(user: User): UserViewModel.UiState.Success {
        return UserViewModel.UiState.Success(
            name = user.name,
            login = user.login,
            avatarUrl = user.avatarUrl,
            company = user.company,
            location = user.location,
            isHireable = user.isHireable,
            followersCount = user.followersCount,
            followingCount = user.followingCount,
        )
    }

}
