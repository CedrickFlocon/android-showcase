package com.cedrickflocon.android.showcase.search.presentation.field

import com.cedrickflocon.android.showcase.search.domain.SearchDataSource
import com.cedrickflocon.android.showcase.search.domain.SearchParams
import javax.inject.Inject

class SearchFieldViewModel @Inject constructor(
    private val dataSource: SearchDataSource
) {

    val search: suspend (String) -> Unit = { query: String ->
        dataSource.events.emit(SearchDataSource.Event.NewSearch(SearchParams(query)))
    }

}