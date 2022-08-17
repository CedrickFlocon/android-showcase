package com.cedrickflocon.android.showcase.search.presentation.field

import com.cedrickflocon.android.showcase.search.domain.SearchParams
import com.cedrickflocon.android.showcase.search.domain.SearchUseCase
import javax.inject.Inject

class SearchFieldViewModel @Inject constructor(
    private val searchUseCase: SearchUseCase
) {

    val search: suspend (String) -> Unit = { query: String ->
        searchUseCase.events.emit(SearchUseCase.Event.NewSearch(SearchParams(query)))
    }

}