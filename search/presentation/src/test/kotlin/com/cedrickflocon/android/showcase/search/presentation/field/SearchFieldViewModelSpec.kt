package com.cedrickflocon.android.showcase.search.presentation.field

import com.cedrickflocon.android.showcase.search.domain.SearchParams
import com.cedrickflocon.android.showcase.search.domain.SearchUseCase
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.coVerify
import io.mockk.mockk

class SearchFieldViewModelSpec : DescribeSpec({
    val searchUseCase = mockk<SearchUseCase>(relaxed = true)
    val viewModel = SearchFieldViewModel(searchUseCase)

    describe("search") {
        viewModel.search("bobby")

        it("should call the datasource") {
            coVerify { searchUseCase.events.emit(SearchUseCase.Event.NewSearch(SearchParams("bobby"))) }
        }
    }
})