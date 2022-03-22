package com.cedrickflocon.android.showcase.search.domain

import java.net.URI

data class SearchResult(
    val searchResultItem: List<SearchResultItem>,
    val pageInfo: PageInfo,
)

data class PageInfo(
    val nextCursor: String?,
)

sealed interface SearchResultItem {
    data class User(
        val email: String,
        val login: String,
        val name: String?,
        val avatarUrl: URI
    ) : SearchResultItem
}

sealed interface SearchError {
    object Network : SearchError
}
