package com.cedrickflocon.android.showcase.search.domain

import app.cash.turbine.testIn
import arrow.core.left
import arrow.core.right
import com.google.common.truth.Truth.assertThat
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.UnconfinedTestDispatcher

@OptIn(ExperimentalCoroutinesApi::class)
class SearchDataSourceSpec : DescribeSpec({
    isolationMode = IsolationMode.InstancePerLeaf
    coroutineTestScope = true

    describe("data source") {
        val useCase = mockk<SearchUseCase>()
        val dataSource = SearchDataSource(useCase, CoroutineScope(UnconfinedTestDispatcher()))

        describe("subscription") {
            val subscription = dataSource.data.testIn(CoroutineScope(UnconfinedTestDispatcher()))
            afterTest {
                assertThat(subscription.cancelAndConsumeRemainingEvents())
                    .isEmpty()
            }

            it("should have a first item") {
                assertThat(subscription.awaitItem())
                    .isEqualTo(
                        SearchDataSource.State(
                            searchParams = SearchParams(query = ""),
                            nextCursor = null,
                            error = false,
                            loading = false,
                            items = null
                        )
                    )
            }

            describe("second subscription") {
                val searchParams = mockk<SearchParams>()
                val secondSubscription = dataSource.data.testIn(CoroutineScope(UnconfinedTestDispatcher()))
                coEvery { useCase.search(searchParams, null) } coAnswers { mockk<SearchResult>(relaxed = true).right() }
                afterTest {
                    assertThat(secondSubscription.cancelAndConsumeRemainingEvents())
                        .isEmpty()
                }

                it("should share result") {
                    dataSource.events.emit(SearchDataSource.Event.NewSearch(searchParams))

                    assertThat(subscription.awaitItem()).isEqualTo(secondSubscription.awaitItem())
                    assertThat(subscription.awaitItem()).isEqualTo(secondSubscription.awaitItem())
                    assertThat(subscription.awaitItem()).isEqualTo(secondSubscription.awaitItem())

                    coVerify(exactly = 1) { useCase.search(searchParams, null) }
                }
            }

            describe("concurrency") {
                val firstSearchParams = mockk<SearchParams>()
                coEvery {
                    useCase.search(firstSearchParams, null)
                } coAnswers {
                    delay(500)
                    mockk<SearchResult>().right()
                }

                val secondSearchParams = mockk<SearchParams>()
                val secondSearchItem = mockk<List<SearchResultItem>>()
                val secondSearchResult = mockk<SearchResult> {
                    every { searchResultItem } returns secondSearchItem
                }
                coEvery {
                    useCase.search(secondSearchParams, null)
                } returns secondSearchResult.right()

                it("should cancel previous search") {
                    val initialState = subscription.awaitItem()

                    dataSource.events.emit(SearchDataSource.Event.NewSearch(firstSearchParams))
                    assertThat(subscription.awaitItem()).isEqualTo(
                        initialState.copy(
                            searchParams = firstSearchParams,
                            loading = true
                        )
                    )

                    dataSource.events.emit(SearchDataSource.Event.NewSearch(secondSearchParams))
                    assertThat(subscription.awaitItem()).isEqualTo(
                        initialState.copy(
                            searchParams = secondSearchParams,
                            loading = true
                        )
                    )

                    val result = subscription.awaitItem()
                    assertThat(result).isEqualTo(
                        initialState.copy(
                            searchParams = secondSearchParams,
                            items = secondSearchItem
                        )
                    )
                }
            }

            describe("retry") {
                dataSource.events.emit(SearchDataSource.Event.Retry)

                it("should do nothing") {
                    assertThat(subscription.awaitItem())
                        .isEqualTo(
                            SearchDataSource.State(
                                searchParams = SearchParams(query = ""),
                                nextCursor = null,
                                error = false,
                                loading = false,
                                items = null
                            )
                        )
                    
                    coVerify { useCase wasNot Called }
                }
            }

            describe("search on success") {
                val searchParams = mockk<SearchParams>()
                val searchItem = mockk<List<SearchResultItem>>()
                val searchResult = mockk<SearchResult> {
                    every { searchResultItem } returns searchItem
                }
                coEvery { useCase.search(searchParams, null) } returns searchResult.right()
                dataSource.events.emit(SearchDataSource.Event.NewSearch(searchParams))

                it("should have a loading state & result") {
                    val initialState = subscription.awaitItem()

                    assertThat(subscription.awaitItem()).isEqualTo(
                        initialState.copy(
                            searchParams = searchParams,
                            loading = true
                        )
                    )

                    val result = subscription.awaitItem()
                    assertThat(result).isEqualTo(
                        initialState.copy(
                            searchParams = searchParams,
                            items = searchItem
                        )
                    )
                }

                describe("second subscription") {
                    val secondSubscription = dataSource.data.testIn(CoroutineScope(UnconfinedTestDispatcher()))
                    afterTest {
                        assertThat(secondSubscription.cancelAndConsumeRemainingEvents())
                            .isEmpty()
                    }

                    it("should have a replay value") {
                        assertThat(subscription.expectMostRecentItem()).isEqualTo(secondSubscription.awaitItem())
                    }
                }
            }

            describe("search on error") {
                val searchParams = mockk<SearchParams>()
                coEvery { useCase.search(searchParams, null) } returns SearchError.Network.left()
                dataSource.events.emit(SearchDataSource.Event.NewSearch(searchParams))

                it("should have an error") {
                    val initialState = subscription.awaitItem()

                    assertThat(subscription.awaitItem()).isEqualTo(
                        initialState.copy(
                            searchParams = searchParams,
                            loading = true
                        )
                    )

                    assertThat(subscription.awaitItem()).isEqualTo(
                        initialState.copy(
                            searchParams = searchParams,
                            error = true
                        )
                    )
                }

                describe("Retry") {
                    val searchItem = mockk<List<SearchResultItem>>()
                    val searchResult = mockk<SearchResult> {
                        every { searchResultItem } returns searchItem
                    }
                    coEvery { useCase.search(searchParams, null) } returns searchResult.right()
                    dataSource.events.emit(SearchDataSource.Event.Retry)

                    it("should retry a search") {
                        val initialState = subscription.awaitItem()
                        subscription.skipItems(2)

                        assertThat(subscription.awaitItem()).isEqualTo(
                            initialState.copy(
                                searchParams = searchParams,
                                loading = true
                            )
                        )

                        assertThat(subscription.awaitItem()).isEqualTo(
                            initialState.copy(
                                searchParams = searchParams,
                                items = searchItem
                            )
                        )
                    }
                }
            }
        }
    }
})