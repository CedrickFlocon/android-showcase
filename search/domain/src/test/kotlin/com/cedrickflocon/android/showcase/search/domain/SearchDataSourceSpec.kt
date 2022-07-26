package com.cedrickflocon.android.showcase.search.domain

import app.cash.turbine.testIn
import arrow.core.left
import arrow.core.right
import com.google.common.truth.Truth.assertThat
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
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
            val subscription = dataSource.states.testIn(CoroutineScope(UnconfinedTestDispatcher()))
            afterTest {
                assertThat(subscription.cancelAndConsumeRemainingEvents())
                    .isEmpty()
            }

            it("should have a first item") {
                assertThat(subscription.awaitItem()).isInstanceOf(SearchDataSource.State.Clean::class.java)
            }

            describe("second subscription") {
                val searchParams = mockk<SearchParams>()
                val secondSubscription = dataSource.states.testIn(CoroutineScope(UnconfinedTestDispatcher()))
                coEvery { useCase.search(searchParams, null) } coAnswers { mockk<SearchResult>().right() }
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
                val secondSearchResult = mockk<SearchResult>()
                coEvery {
                    useCase.search(secondSearchParams, null)
                } returns secondSearchResult.right()

                it("should cancel previous search") {
                    assertThat(subscription.awaitItem()).isInstanceOf(SearchDataSource.State.Clean::class.java)

                    dataSource.events.emit(SearchDataSource.Event.NewSearch(firstSearchParams))

                    val loading = subscription.awaitItem()
                    assertThat(loading).isInstanceOf(SearchDataSource.State.Loading::class.java)

                    dataSource.events.emit(SearchDataSource.Event.NewSearch(secondSearchParams))
                    val result = subscription.awaitItem()
                    assertThat((result as SearchDataSource.State.Result).searchResult).isEqualTo(secondSearchResult)
                }
            }

            describe("search on success") {
                val searchParams = mockk<SearchParams>()
                val searchResult = mockk<SearchResult>()
                coEvery { useCase.search(searchParams, null) } returns searchResult.right()
                dataSource.events.emit(SearchDataSource.Event.NewSearch(searchParams))

                it("should have a loading state & result") {
                    assertThat(subscription.awaitItem()).isInstanceOf(SearchDataSource.State.Clean::class.java)
                    assertThat(subscription.awaitItem()).isInstanceOf(SearchDataSource.State.Loading::class.java)
                    assertThat((subscription.awaitItem() as SearchDataSource.State.Result).searchResult).isEqualTo(searchResult)
                }

                describe("second subscription") {
                    val secondSubscription = dataSource.states.testIn(CoroutineScope(UnconfinedTestDispatcher()))
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
                    assertThat(subscription.awaitItem()).isInstanceOf(SearchDataSource.State.Clean::class.java)
                    assertThat(subscription.awaitItem()).isInstanceOf(SearchDataSource.State.Loading::class.java)
                    assertThat(subscription.awaitItem()).isInstanceOf(SearchDataSource.State.Error::class.java)
                }
            }
        }
    }
})