package com.cedrickflocon.android.showcase.search.presentation

import com.cedrickflocon.android.showcase.search.domain.SearchResult
import com.cedrickflocon.android.showcase.search.domain.SearchResultItem
import javax.inject.Inject

class UiStateMapper @Inject constructor() : (SearchResult) -> SearchViewModel.UiState.Success {

    override fun invoke(searchResult: SearchResult): SearchViewModel.UiState.Success {
        return SearchViewModel.UiState.Success(
            searchResult.searchResultItem
                .filterIsInstance<SearchResultItem.User>()
                .map {
                    SearchViewModel.UiState.Success.Item.User(
                        it.email,
                        it.login,
                        it.avatarUrl
                    )
                })
    }

}
