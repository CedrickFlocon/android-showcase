package com.cedrickflocon.android.showcase.user.domain

import arrow.core.right
import com.google.common.truth.Truth.assertThat
import io.kotest.core.spec.style.DescribeSpec
import java.net.URL

class UserUseCaseImplSpec : DescribeSpec({
    val useCase = UserUseCaseImpl()

    it("should return dummy user") {
        assertThat(useCase.getUser("Me"))
            .isEqualTo(User("Me", URL("https://avatars.githubusercontent.com/u/7161169?s=96&v=4")).right())
    }

})
