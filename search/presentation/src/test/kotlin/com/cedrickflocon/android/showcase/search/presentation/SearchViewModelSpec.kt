package com.cedrickflocon.android.showcase.search.presentation

import arrow.core.left
import arrow.core.right
import com.cedrickflocon.android.showcase.search.domain.SearchError
import com.cedrickflocon.android.showcase.search.domain.SearchResult
import com.cedrickflocon.android.showcase.search.domain.SearchUseCase
import com.cedrickflocon.android.showcase.user.router.UserParams
import com.cedrickflocon.android.showcase.user.router.UserRouter
import com.google.common.truth.Truth.assertThat
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.*
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList

class SearchViewModelSpec : DescribeSpec({
    val useCase = mockk<SearchUseCase>()
    val mapper = mockk<UiStateMapper>()
    val router = mockk<UserRouter>(relaxUnitFun = true)
    val viewModel = SearchViewModel(useCase, mapper, router)

    describe("search on success") {
        val success = mockk<SearchViewModel.UiState.Success>()
        val onClickItem = slot<(String) -> Unit>()
        beforeEach {
            val searchResult = mockk<SearchResult>()
            coEvery { useCase.search("Cedrick") } returns searchResult.right()
            every { mapper(searchResult, capture(onClickItem)) } returns success
        }

        it("should have loading & success") {
            assertThat(viewModel.data.count()).isEqualTo(2)
            assertThat(viewModel.data.take(2).toList())
                .containsExactlyElementsIn(
                    listOf(
                        SearchViewModel.UiState.Loading,
                        success
                    )
                )
        }

        describe("click on item") {
            beforeEach {
                onClickItem.captured("UncleBob")
            }

            it("should router to user") {
                verify { router.navigateToUser(UserParams("UncleBob")) }
            }
        }
    }

    describe("search on error") {
        beforeTest {
            coEvery { useCase.search("Cedrick") } returns SearchError.Network.left()
        }

        it("should have loading & error") {
            assertThat(viewModel.data.count()).isEqualTo(2)
            assertThat(viewModel.data.take(2).toList())
                .containsExactlyElementsIn(
                    listOf(
                        SearchViewModel.UiState.Loading,
                        SearchViewModel.UiState.Error
                    )
                )
        }
    }

})
