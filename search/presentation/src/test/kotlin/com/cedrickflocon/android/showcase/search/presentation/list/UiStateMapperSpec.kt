package com.cedrickflocon.android.showcase.search.presentation.list

import com.cedrickflocon.android.showcase.search.domain.SearchResultItem
import com.google.common.truth.Truth.assertThat
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.mockk
import io.mockk.verify
import java.net.URI

class UiStateMapperSpec : DescribeSpec({
    isolationMode = IsolationMode.InstancePerLeaf
    coroutineTestScope = true

    val mapper = UiStateMapper()

    describe("Search Result") {
        val onClick = mockk<((SearchResultItem.User) -> Unit)>(relaxed = true)
        val searchResultItems = listOf(
            mockk<SearchResultItem>(),
            SearchResultItem.User(
                "unclebob@showcase.com",
                "UncleBob",
                "Robert Cecil Martin",
                URI("http://robert.cecil.martin.com")
            ),
            mockk<SearchResultItem>(),
            SearchResultItem.User(
                "bobby@showcase.com",
                "Bobby",
                null,
                URI("http://bobby.com")
            ),
        )

        describe("map") {
            val items = mapper(searchResultItems, onClick)

            it("can be transform to a success") {
                assertThat(items).hasSize(2)

                with(items[0]) {
                    assertThat(this.loading).isEqualTo(false)
                    assertThat(this.email).isEqualTo("unclebob@showcase.com")
                    assertThat(this.login).isEqualTo("UncleBob")
                    assertThat(this.avatarUrl).isEqualTo(URI("http://robert.cecil.martin.com"))
                    this.onClickItem()
                    verify { onClick(searchResultItems[1] as SearchResultItem.User) }
                }

                with(items[1]) {
                    assertThat(this.loading).isEqualTo(false)
                    assertThat(this.email).isEqualTo("bobby@showcase.com")
                    assertThat(this.login).isEqualTo("Bobby")
                    assertThat(this.avatarUrl).isEqualTo(URI("http://bobby.com"))
                    this.onClickItem()
                    verify { onClick(searchResultItems[3] as SearchResultItem.User) }
                }
            }
        }
    }
})