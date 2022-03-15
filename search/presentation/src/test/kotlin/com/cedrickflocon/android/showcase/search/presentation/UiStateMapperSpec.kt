package com.cedrickflocon.android.showcase.search.presentation

import com.cedrickflocon.android.showcase.search.domain.SearchResult
import com.cedrickflocon.android.showcase.search.domain.SearchResultItem
import com.google.common.truth.Truth.assertThat
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.mockk
import java.net.URI

class UiStateMapperSpec : DescribeSpec({
    val mapper = UiStateMapper()

    describe("Search Result") {
        val searchResult = SearchResult(
            listOf(
                mockk(),
                SearchResultItem.User(
                    "unclebob@showcase.com",
                    "UncleBob",
                    "Robert Cecil Martin",
                    URI("http://robert.cecil.martin.com")
                ),
                mockk(),
                SearchResultItem.User(
                    "bobby@showcase.com",
                    "Bobby",
                    null,
                    URI("http://bobby.com")
                ),
            )
        )

        it("can be transform to a success") {
            assertThat(mapper(searchResult))
                .isEqualTo(
                    SearchViewModel.UiState.Success(
                        listOf<SearchViewModel.UiState.Success.Item>(
                            SearchViewModel.UiState.Success.Item.User(
                                "unclebob@showcase.com",
                                "UncleBob",
                                URI("http://robert.cecil.martin.com")
                            ),
                            SearchViewModel.UiState.Success.Item.User(
                                "bobby@showcase.com",
                                "Bobby",
                                URI("http://bobby.com")
                            )
                        )
                    )
                )
        }
    }
})
