package com.cedrickflocon.android.showcase.search.presentation.field

import com.cedrickflocon.android.showcase.search.domain.SearchDataSource
import com.cedrickflocon.android.showcase.search.domain.SearchParams
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.coVerify
import io.mockk.mockk

class SearchFIeldViewModelSpec : DescribeSpec({
    val dataSource = mockk<SearchDataSource>(relaxed = true)
    val viewModel = SearchFieldViewModel(dataSource)

    describe("search") {
        viewModel.search("bobby")

        it("should call the datasource") {
            coVerify { dataSource.events.emit(SearchDataSource.Event.NewSearch(SearchParams("bobby"))) }
        }
    }
})