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

    val dataSourceState = MutableSharedFlow<SearchDataSource.State>()
    val dataSource = mockk<SearchDataSource>(relaxed = true) {
        every { data } returns dataSourceState
    }
    val mapper = mockk<UiStateMapper>()
    val router = mockk<UserRouter>(relaxUnitFun = true)
    val viewModel = SearchListViewModel(dataSource, mapper, router)

    describe("initial state") {
        val initialState = viewModel.initialState

        it("should be empty") {
            assertThat(initialState.error).isFalse()
            assertThat(initialState.items).isNull()
        }

        describe("call on scroll") {
            initialState.onScroll(0)

            it("should not notify the datasource") {
                verify { dataSource.events wasNot Called }
            }
        }
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

        describe("first state") {
            val initialState = subscription.awaitItem()

            it("should be empty") {
                assertThat(initialState.error).isFalse()
                assertThat(initialState.items).isNull()
            }

            describe("call on scroll") {
                initialState.onScroll(0)

                it("should not notify the datasource") {
                    verify { dataSource.events wasNot Called }
                }
            }
        }

        describe("clean state") {
            dataSourceState.emit(initialDataSourceState)

            it("should be empty") {
                val initialState = subscription.awaitItem()
                assertThat(initialState.error).isFalse()
                assertThat(initialState.items).isNull()
            }
        }

        describe("error state") {
            dataSourceState.emit(initialDataSourceState.copy(error = true))

            it("last item should have an error") {
                subscription.skipItems(1)

                val errorState = subscription.awaitItem()
                assertThat(errorState.error).isTrue()
                assertThat(errorState.items).isNull()
            }
        }

        describe("loading state") {
            dataSourceState.emit(initialDataSourceState.copy(loading = true))

            it("last item should have loading items") {
                subscription.skipItems(1)

                val loading = subscription.awaitItem()
                assertThat(loading.items).hasSize(3)
                assertThat(loading.error).isFalse()

                loading.items!!.forEach {
                    assertThat(it.loading).isTrue()
                    assertThat(it.login.length).isIn(10..20)
                    assertThat(it.email.length).isIn(10..20)
                }
            }
        }

        describe("empty state") {
            every { mapper(emptyList(), any()) } returns emptyList()
            dataSourceState.emit(initialDataSourceState.copy(items = emptyList()))

            it("should empty ui state") {
                subscription.skipItems(1)

                val success = subscription.awaitItem()
                assertThat(success.items).isEmpty()
                assertThat(success.error).isFalse()
            }
        }

        describe("result state") {
            val items = listOf<SearchListViewModel.UiState.Item>(mockk(), mockk(), mockk())
            val searchResultItem = listOf<SearchResultItem>(mockk(), mockk(), mockk())
            val lambda = slot<(suspend (SearchResultItem.User) -> Unit)>()
            every { mapper(searchResultItem, capture(lambda)) } returns items
            dataSourceState.emit(initialDataSourceState.copy(items = searchResultItem))

            it("last item should have result items") {
                subscription.skipItems(1)

                val success = subscription.awaitItem()
                assertThat(success.items).isEqualTo(items)
                assertThat(success.error).isFalse()

                lambda.captured.invoke(mockk { every { login } returns "bobby" })
                verify { router.navigateToUser(UserParams("bobby")) }
            }

            describe("OnScroll second item") {
                subscription.expectMostRecentItem().onScroll(1)

                it("should not call the datasource") {
                    verify { dataSource.events wasNot Called }
                }
            }

            describe("OnScroll to last item") {
                subscription.expectMostRecentItem().onScroll(2)

                it("should call the datasource") {
                    coVerify { dataSource.events.emit(SearchDataSource.Event.NextPage) }
                }
            }

            describe("loading with result") {
                dataSourceState.emit(initialDataSourceState.copy(items = searchResultItem, loading = true))

                it("last item should have result items & error") {
                    subscription.skipItems(2)

                    val success = subscription.awaitItem()
                    assertThat(success.items).hasSize(6)
                    assertThat(success.items!!.take(3)).isEqualTo(items)
                    success.items!!.takeLast(3).forEach {
                        assertThat(it.loading).isTrue()
                        assertThat(it.login.length).isIn(10..20)
                        assertThat(it.email.length).isIn(10..20)
                    }
                    assertThat(success.error).isFalse()

                    lambda.captured.invoke(mockk { every { login } returns "bobby" })
                    verify { router.navigateToUser(UserParams("bobby")) }
                }
            }

            describe("error with result") {
                dataSourceState.emit(initialDataSourceState.copy(items = searchResultItem, error = true))

                it("last item should have result items & error") {
                    subscription.skipItems(2)

                    val success = subscription.awaitItem()
                    assertThat(success.items).isEqualTo(items)
                    assertThat(success.error).isTrue()

                    lambda.captured.invoke(mockk { every { login } returns "bobby" })
                    verify { router.navigateToUser(UserParams("bobby")) }
                }
            }
        }
    }
})