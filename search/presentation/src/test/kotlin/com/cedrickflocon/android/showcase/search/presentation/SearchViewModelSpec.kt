package com.cedrickflocon.android.showcase.search.presentation

import arrow.core.left
import arrow.core.right
import com.cedrickflocon.android.showcase.search.domain.SearchError
import com.cedrickflocon.android.showcase.search.domain.SearchResult
import com.cedrickflocon.android.showcase.search.domain.SearchUseCase
import com.google.common.truth.Truth
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList

class SearchViewModelSpec : DescribeSpec({
    val useCase = mockk<SearchUseCase>()
    val mapper = mockk<UiStateMapper>()
    val viewModel = SearchViewModel(useCase, mapper)

    describe("search on success") {
        val success = mockk<SearchViewModel.UiState.Success>()
        beforeEach {
            val searchResult = mockk<SearchResult>()
            coEvery { useCase.search("Cedrick") } returns searchResult.right()
            every { mapper(searchResult) } returns success
        }

        it("should have loading & success") {
            Truth.assertThat(viewModel.data.count()).isEqualTo(2)
            Truth.assertThat(viewModel.data.take(2).toList())
                .containsExactlyElementsIn(
                    listOf(
                        SearchViewModel.UiState.Loading,
                        success
                    )
                )
        }
    }

    describe("search on error") {
        beforeTest {
            coEvery { useCase.search("Cedrick") } returns SearchError.Network.left()
        }

        it("should have loading & error") {
            Truth.assertThat(viewModel.data.count()).isEqualTo(2)
            Truth.assertThat(viewModel.data.take(2).toList())
                .containsExactlyElementsIn(
                    listOf(
                        SearchViewModel.UiState.Loading,
                        SearchViewModel.UiState.Error
                    )
                )
        }
    }

})
