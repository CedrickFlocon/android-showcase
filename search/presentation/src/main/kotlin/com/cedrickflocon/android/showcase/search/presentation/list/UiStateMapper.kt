package com.cedrickflocon.android.showcase.search.presentation.list

import com.cedrickflocon.android.showcase.search.domain.SearchResult
import com.cedrickflocon.android.showcase.search.domain.SearchResultItem
import javax.inject.Inject

class UiStateMapper @Inject constructor() : (SearchResult, (login: String) -> Unit) -> List<SearchListViewModel.UiState.Item> {

    override fun invoke(searchResult: SearchResult, onClick: (login: String) -> Unit): List<SearchListViewModel.UiState.Item> {
        return searchResult.searchResultItem
            .filterIsInstance<SearchResultItem.User>()
            .map {
                SearchListViewModel.UiState.Item(
                    loading = false,
                    email = it.email,
                    login = it.login,
                    avatarUrl = it.avatarUrl,
                    onClickItem = { onClick(it.login) }
                )
            }
    }

}