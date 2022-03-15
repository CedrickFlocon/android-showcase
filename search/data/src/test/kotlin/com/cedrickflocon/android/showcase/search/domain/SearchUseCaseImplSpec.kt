package com.cedrickflocon.android.showcase.search.domain

import arrow.core.Either
import com.google.common.truth.Truth.assertThat
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.coEvery
import io.mockk.mockk

class SearchUseCaseImplSpec : DescribeSpec({
    val repository = mockk<SearchRepository>()
    val useCase = SearchUseCaseImpl(repository)

    describe("search") {
        val search = mockk<Either<SearchError, SearchResult>>()
        beforeEach { coEvery { repository.search("Robert Cecil Martin") } returns search }

        it("should return the given search") {
            assertThat(useCase.search("Robert Cecil Martin")).isEqualTo(search)
        }
    }

})
