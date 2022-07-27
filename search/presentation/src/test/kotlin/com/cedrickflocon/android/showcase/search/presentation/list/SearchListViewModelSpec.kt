package com.cedrickflocon.android.showcase.search.presentation.list

import app.cash.turbine.testIn
import com.cedrickflocon.android.showcase.search.domain.SearchDataSource
import com.cedrickflocon.android.showcase.search.domain.SearchResultItem
import com.cedrickflocon.android.showcase.user.router.UserParams
import com.cedrickflocon.android.showcase.user.router.UserRouter
import com.google.common.truth.Truth.assertThat
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher

class SearchListViewModelSpec : DescribeSpec({
    isolationMode = IsolationMode.InstancePerLeaf
    coroutineTestScope = true

    val dataSource = mockk<SearchDataSource>()
    val dataSourceState = MutableSharedFlow<SearchDataSource.State>()
    every { dataSource.data } returns dataSourceState
    val mapper = mockk<UiStateMapper>()
    val router = mockk<UserRouter>(relaxUnitFun = true)
    val viewModel = SearchListViewModel(dataSource, mapper, router)

    it("should have an initial state") {
        assertThat(viewModel.initialState)
            .isEqualTo(SearchListViewModel.UiState(null, false))
    }

    describe("subscription") {
        val subscription = viewModel.states.testIn(CoroutineScope(UnconfinedTestDispatcher()))
        val initialDataSourceState = SearchDataSource.State(
            searchParams = mockk(),
            nextCursor = null,
            error = false,
            loading = false,
            items = null
        )

        afterTest {
            assertThat(subscription.cancelAndConsumeRemainingEvents())
                .isEmpty()
        }

        it("should have an initialState") {
            assertThat(subscription.awaitItem())
                .isEqualTo(SearchListViewModel.UiState(null, false))
        }

        describe("clean state") {
            dataSourceState.emit(initialDataSourceState)

            it("should not emit a new state") {
                assertThat(subscription.awaitItem())
                    .isEqualTo(SearchListViewModel.UiState(null, false))
            }
        }

        describe("error state") {
            dataSourceState.emit(initialDataSourceState.copy(error = true))

            it("last item should have an error") {
                subscription.skipItems(1)

                assertThat(subscription.awaitItem())
                    .isEqualTo(SearchListViewModel.UiState(null, true))
            }
        }

        describe("loading state") {
            dataSourceState.emit(initialDataSourceState.copy(loading = true))

            it("last item should have loading items") {
                subscription.skipItems(1)

                val loading = subscription.awaitItem()
                assertThat(loading.items).hasSize(3)
                assertThat(loading.error).isFalse()

                loading.items?.forEach {
                    assertThat(it.loading).isTrue()
                    assertThat(it.login.length).isIn(10..20)
                    assertThat(it.email.length).isIn(10..20)
                }
            }
        }

        describe("result state") {
            val items = mockk<List<SearchListViewModel.UiState.Item>>()
            val searchResultItem = mockk<List<SearchResultItem>>()
            val lambda = slot<(String) -> Unit>()
            every { mapper(searchResultItem, capture(lambda)) } returns items
            dataSourceState.emit(initialDataSourceState.copy(items = searchResultItem))

            it("last item should have result items") {
                subscription.skipItems(1)

                val success = subscription.awaitItem()
                assertThat(success.items).isEqualTo(items)
                assertThat(success.error).isFalse()

                lambda.invoke("bobby")
                verify { router.navigateToUser(UserParams("bobby")) }
            }
        }
    }
})