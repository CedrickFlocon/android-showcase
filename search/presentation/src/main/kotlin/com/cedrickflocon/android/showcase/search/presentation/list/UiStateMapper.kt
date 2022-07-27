package com.cedrickflocon.android.showcase.search.presentation.list

import com.cedrickflocon.android.showcase.search.domain.SearchResultItem
import javax.inject.Inject

class UiStateMapper @Inject constructor() : (List<SearchResultItem>, (login: String) -> Unit) -> List<SearchListViewModel.UiState.Item> {

    override fun invoke(searchResultItems: List<SearchResultItem>, onClick: (login: String) -> Unit): List<SearchListViewModel.UiState.Item> {
        return searchResultItems
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
