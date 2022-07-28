package com.cedrickflocon.android.showcase.search.data

import com.cedrickflocon.android.showcase.search.domain.PageInfo
import com.cedrickflocon.android.showcase.search.domain.SearchResult
import com.cedrickflocon.android.showcase.search.domain.SearchResultItem
import com.cedrickflocon.android.showcase.user.data.SearchQuery
import com.google.common.truth.Truth.assertThat
import io.kotest.core.spec.style.DescribeSpec
import java.net.URI

class SearchMapperSpec : DescribeSpec({
    val mapper = SearchMapper()

    describe("search") {
        val search = SearchQuery.Search(
            listOf(
                SearchQuery.Node(
                    "",
                    SearchQuery.OnUser(
                        "unclebob@showcase.com",
                        "UncleBob",
                        "Robert Cecil Martin",
                        URI("http://robert.cecil.martin.com")
                    )
                ),
                null,
                SearchQuery.Node(
                    "",
                    SearchQuery.OnUser(
                        "bobby@showcase.com",
                        "Bobby",
                        null,
                        URI("http://bobby.com")
                    )
                ),
            ),
            SearchQuery.PageInfo("Y3Vyc29yOjEw")
        )

        it("can transform the search") {
            assertThat(mapper(search)).isEqualTo(
                SearchResult(
                    listOf(
                        SearchResultItem.User(
                            "unclebob@showcase.com",
                            "UncleBob",
                            "Robert Cecil Martin",
                            URI("http://robert.cecil.martin.com")
                        ),
                        SearchResultItem.User(
                            "bobby@showcase.com",
                            "Bobby",
                            null,
                            URI("http://bobby.com")
                        ),
                    ),
                    PageInfo("Y3Vyc29yOjEw")
                )
            )
        }
    }
})