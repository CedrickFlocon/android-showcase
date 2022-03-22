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
        beforeEach { coEvery { repository.search(SearchParams("Robert Cecil Martin", "Y3Vyc29yOjEw")) } returns search }

        it("should return the given search") {
            assertThat(useCase.search(SearchParams("Robert Cecil Martin", "Y3Vyc29yOjEw")))
                .isEqualTo(search)
        }
    }

})
