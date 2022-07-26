package com.cedrickflocon.android.showcase.search.presentation.list

import com.cedrickflocon.android.showcase.search.domain.SearchResult
import com.cedrickflocon.android.showcase.search.domain.SearchResultItem
import com.google.common.truth.Truth
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
            lateinit var items: List<SearchListViewModel.UiState.Item>
            beforeEach { items = mapper(searchResult, onClick) }

            it("can be transform to a success") {
                Truth.assertThat(items).hasSize(2)

                with(items[0]) {
                    Truth.assertThat(this.loading).isEqualTo(false)
                    Truth.assertThat(this.email).isEqualTo("unclebob@showcase.com")
                    Truth.assertThat(this.login).isEqualTo("UncleBob")
                    Truth.assertThat(this.avatarUrl).isEqualTo(URI("http://robert.cecil.martin.com"))
                }

                with(items[1]) {
                    Truth.assertThat(this.loading).isEqualTo(false)
                    Truth.assertThat(this.email).isEqualTo("bobby@showcase.com")
                    Truth.assertThat(this.login).isEqualTo("Bobby")
                    Truth.assertThat(this.avatarUrl).isEqualTo(URI("http://bobby.com"))
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