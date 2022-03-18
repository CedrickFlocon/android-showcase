package com.cedrickflocon.android.showcase.search.presentation

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import com.cedrickflocon.android.showcase.search.domain.SearchError
import com.cedrickflocon.android.showcase.search.domain.SearchResult
import com.cedrickflocon.android.showcase.search.domain.SearchUseCase
import com.cedrickflocon.android.showcase.user.router.UserRouter
import com.google.common.truth.Truth.assertThat
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.*

class SearchViewModelSpec : DescribeSpec({
    val useCase = mockk<SearchUseCase>()
    val mapper = mockk<UiStateMapper>()
    val router = mockk<UserRouter>(relaxUnitFun = true)
    val viewModel = SearchViewModel(useCase, mapper, router)

    describe("initial state") {
        val initialState = viewModel.initialState

        describe("initial state onSearch") {
            initialState.onSearch("Never proceed")

            it("should not call the use case") {
                verify { useCase wasNot Called }
            }
        }

        it("should have an initial state") {
            assertThat(initialState.loading).isFalse()
            assertThat(initialState.error).isFalse()
            assertThat(initialState.items).isNull()
        }
    }

    it("should init states with initialState") {
        viewModel.states.test {
            val first = awaitItem()

            assertThat(first.loading).isFalse()
            assertThat(first.error).isFalse()
            assertThat(first.items).isNull()

            expectNoEvents()
        }
    }

    describe("success search") {
        val items = mockk<List<SearchViewModel.UiState.Item>>()
        val searchResult = mockk<SearchResult>()
        coEvery { useCase.search("bobby") } returns searchResult.right()
        every { mapper(searchResult, any()) } returns items

        it("should have loading & success") {
            viewModel.states.test {
                awaitItem().onSearch("bobby")

                val loading = awaitItem()
                assertThat(loading.loading).isTrue()
                assertThat(loading.items).isNull()
                assertThat(loading.error).isFalse()

                val success = awaitItem()
                assertThat(success.loading).isFalse()
                assertThat(success.items).isEqualTo(items)
                assertThat(success.error).isFalse()
            }
        }
    }

    describe("error search") {
        coEvery { useCase.search("bobby") } returns SearchError.Network.left()

        it("should have loading & success") {
            viewModel.states.test {
                awaitItem().onSearch("bobby")

                val loading = awaitItem()
                assertThat(loading.loading).isTrue()
                assertThat(loading.items).isNull()
                assertThat(loading.error).isFalse()

                val error = awaitItem()
                assertThat(error.loading).isFalse()
                assertThat(error.items).isNull()
                assertThat(error.error).isTrue()
            }
        }
    }
})
