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
class SearchUseCaseImplSpec : DescribeSpec({
    isolationMode = IsolationMode.InstancePerLeaf
    coroutineTestScope = true

    describe("data source") {
        val repository = mockk<SearchRepository>()
        val dataSource = SearchUseCaseImpl(repository, CoroutineScope(UnconfinedTestDispatcher()))

        describe("subscription") {
            val subscription = dataSource.search.testIn(CoroutineScope(UnconfinedTestDispatcher()))

            val initialState = SearchUseCase.State(
                searchParams = mockk(),
                hasNext = false,
                loading = false,
                error = false,
                items = null,
            )

            afterTest {
                assertThat(subscription.cancelAndConsumeRemainingEvents())
                    .isEmpty()
            }

            describe("second subscription") {
                val searchParams = mockk<SearchParams>()
                val secondSubscription = dataSource.search.testIn(CoroutineScope(UnconfinedTestDispatcher()))
                coEvery { repository.search(searchParams, null) } coAnswers { mockk<SearchResult>(relaxed = true).right() }
                afterTest {
                    assertThat(secondSubscription.cancelAndConsumeRemainingEvents())
                        .isEmpty()
                }

                it("should share result") {
                    dataSource.events.emit(SearchUseCase.Event.NewSearch(searchParams))

                    assertThat(subscription.awaitItem()).isEqualTo(secondSubscription.awaitItem())
                    assertThat(subscription.awaitItem()).isEqualTo(secondSubscription.awaitItem())

                    coVerify(exactly = 1) { repository.search(searchParams, null) }
                }
            }

            describe("concurrency") {
                val firstSearchParams = mockk<SearchParams>()
                coEvery {
                    repository.search(firstSearchParams, null)
                } coAnswers {
                    delay(500)
                    mockk<SearchResult>().right()
                }

                val secondSearchParams = mockk<SearchParams>()
                val secondSearchItem = listOf<SearchResultItem>(mockk(), mockk(), mockk())
                val secondSearchResult = mockk<SearchResult> {
                    every { searchResultItem } returns secondSearchItem
                    every { pageInfo.nextCursor } returns "firstCursor"
                }
                coEvery {
                    repository.search(secondSearchParams, null)
                } returns secondSearchResult.right()

                it("should cancel previous search") {
                    dataSource.events.emit(SearchUseCase.Event.NewSearch(firstSearchParams))
                    assertThat(subscription.awaitItem()).isEqualTo(
                        initialState.copy(
                            searchParams = firstSearchParams,
                            loading = true
                        )
                    )

                    dataSource.events.emit(SearchUseCase.Event.NewSearch(secondSearchParams))
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
                            hasNext = true,
                            items = secondSearchItem,
                        )
                    )
                }
            }

            describe("invalid event") {
                listOf(
                    SearchUseCase.Event.Retry,
                    SearchUseCase.Event.NextPage
                ).forEach {
                    describe("Event : $it") {
                        dataSource.events.emit(it)

                        it("should do nothing") {
                            coVerify { repository wasNot Called }
                        }
                    }
                }
            }

            describe("search on success") {
                val searchParams = mockk<SearchParams>()
                val firstPageItem = listOf<SearchResultItem>(mockk(), mockk(), mockk())
                val searchResult = mockk<SearchResult> {
                    every { searchResultItem } returns firstPageItem
                    every { pageInfo.nextCursor } returns "firstCursor"
                }
                coEvery { repository.search(searchParams, null) } returns searchResult.right()
                dataSource.events.emit(SearchUseCase.Event.NewSearch(searchParams))

                it("should have a loading state & result") {
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
                            hasNext = true,
                            items = firstPageItem,
                        )
                    )
                }

                describe("last page") {
                    val secondPageITem = listOf<SearchResultItem>(mockk(), mockk(), mockk())
                    val newSearchResult = mockk<SearchResult> {
                        every { searchResultItem } returns secondPageITem
                        every { pageInfo.nextCursor } returns null
                    }
                    coEvery { repository.search(searchParams, "firstCursor") } returns newSearchResult.right()
                    dataSource.events.emit(SearchUseCase.Event.NextPage)

                    it("should have loading & new items state") {
                        subscription.skipItems(1)
                        val successState = subscription.awaitItem()
                        assertThat(successState.items).isEqualTo(firstPageItem)


                        assertThat(subscription.awaitItem()).isEqualTo(
                            successState.copy(
                                loading = true
                            )
                        )

                        val result = subscription.awaitItem()
                        assertThat(result).isEqualTo(
                            successState.copy(
                                items = firstPageItem + secondPageITem,
                                hasNext = false
                            )
                        )
                    }

                    describe("Next Page") {
                        dataSource.events.emit(SearchUseCase.Event.NextPage)

                        it("should not produce state") {
                            subscription.skipItems(4)
                            coVerify(exactly = 2) { repository.search(any(), any()) }
                        }
                    }
                }

                describe("second subscription") {
                    val secondSubscription = dataSource.search.testIn(CoroutineScope(UnconfinedTestDispatcher()))
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
                coEvery { repository.search(searchParams, null) } returns SearchError.Network.left()
                dataSource.events.emit(SearchUseCase.Event.NewSearch(searchParams))

                it("should have an error") {
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

                describe("retry") {
                    val searchItem = listOf<SearchResultItem>(mockk(), mockk(), mockk())
                    val searchResult = mockk<SearchResult> {
                        every { searchResultItem } returns searchItem
                        every { pageInfo.nextCursor } returns "firstCursor"
                    }
                    coEvery { repository.search(searchParams, null) } returns searchResult.right()
                    dataSource.events.emit(SearchUseCase.Event.Retry)

                    it("should retry a search") {
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
                                hasNext = true,
                                items = searchItem
                            )
                        )
                    }
                }
            }
        }
    }
})
