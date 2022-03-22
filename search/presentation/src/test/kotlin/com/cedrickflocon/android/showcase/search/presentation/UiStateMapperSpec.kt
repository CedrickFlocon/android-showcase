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
            ),
            mockk()
        )

        describe("map") {
            lateinit var items: List<SearchViewModel.UiState.Item>
            beforeEach { items = mapper(searchResult, onClick) }

            it("can be transform to a success") {
                assertThat(items).hasSize(2)

                with(items[0]) {
                    assertThat(this.loading).isEqualTo(false)
                    assertThat(this.email).isEqualTo("unclebob@showcase.com")
                    assertThat(this.login).isEqualTo("UncleBob")
                    assertThat(this.avatarUrl).isEqualTo(URI("http://robert.cecil.martin.com"))
                }

                with(items[1]) {
                    assertThat(this.loading).isEqualTo(false)
                    assertThat(this.email).isEqualTo("bobby@showcase.com")
                    assertThat(this.login).isEqualTo("Bobby")
                    assertThat(this.avatarUrl).isEqualTo(URI("http://bobby.com"))
                }
            }

            items.forEach { item ->
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
