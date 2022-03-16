package com.cedrickflocon.android.showcase.search.presentation

import com.cedrickflocon.android.showcase.search.domain.SearchResult
import com.cedrickflocon.android.showcase.search.domain.SearchResultItem
import com.google.common.truth.Truth.assertThat
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.mockk
import io.mockk.verify
import java.net.URI

class UiStateMapperSpec : DescribeSpec({
    val mapper = UiStateMapper()

    describe("Search Result") {
        val onClick = mockk<(String) -> Unit>(relaxed = true)
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

        describe("map") {
            lateinit var success: SearchViewModel.UiState.Success
            beforeEach { success = mapper(searchResult, onClick) }

            it("can be transform to a success") {
                assertThat(success.items).hasSize(2)

                with(success.items[0] as SearchViewModel.UiState.Success.Item.User) {
                    assertThat(this.login).isEqualTo("UncleBob")
                    assertThat(this.email).isEqualTo("unclebob@showcase.com")
                    assertThat(this.avatarUrl).isEqualTo(URI("http://robert.cecil.martin.com"))
                }

                with(success.items[1] as SearchViewModel.UiState.Success.Item.User) {
                    assertThat(this.login).isEqualTo("Bobby")
                    assertThat(this.email).isEqualTo("bobby@showcase.com")
                    assertThat(this.avatarUrl).isEqualTo(URI("http://bobby.com"))
                }
            }

            success.items
                .filterIsInstance<SearchViewModel.UiState.Success.Item.User>()
                .forEach { item ->
                    describe("click on item ${item.login}") {
                        beforeEach { item.onClickItem() }

                        it("should call onclick") {
                            verify { onClick(item.login) }
                        }
                    }
                }
        }
    }
})
