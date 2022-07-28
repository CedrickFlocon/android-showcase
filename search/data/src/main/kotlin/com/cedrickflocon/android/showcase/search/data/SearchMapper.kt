package com.cedrickflocon.android.showcase.search.data

import com.cedrickflocon.android.showcase.search.domain.PageInfo
import com.cedrickflocon.android.showcase.search.domain.SearchResult
import com.cedrickflocon.android.showcase.search.domain.SearchResultItem
import com.cedrickflocon.android.showcase.user.data.SearchQuery
import javax.inject.Inject

class SearchMapper @Inject constructor() : (SearchQuery.Search) -> SearchResult {

    override fun invoke(gqlSearch: SearchQuery.Search): SearchResult {
        return SearchResult(
            searchResultItem = gqlSearch.nodes
                ?.mapNotNull { it?.onUser }
                ?.map {
                    SearchResultItem.User(
                        email = it.email,
                        login = it.login,
                        name = it.name,
                        avatarUrl = it.avatarUrl
                    )
                } ?: emptyList(),
            pageInfo = PageInfo(nextCursor = gqlSearch.pageInfo.endCursor))
    }

}
