package fernandocs.list.item

import fernandocs.domain.GetItems
import fernandocs.domain.Item
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class ItemsPresenter @Inject constructor(
    private val getItems: GetItems,
    private val mapper: ItemViewStateMapper,
    private val view: ItemsView
) {

    fun getStates(viewState: ListViewState): Observable<ListViewState> {
        val initialReducer =
            if (viewState.isInitial) loadPosts()
            else Observable.empty()
        val intentReducer = view.getIntents().flatMap(::handleIntent)
        return initialReducer.concatWith(intentReducer)
            .scan(viewState) { previousState, reducer ->
                reducer(previousState)
            }.onErrorResumeNext { t: Throwable ->
                getStates(ListViewState(hasError = true))
            }
    }

    private fun handleIntent(
        intent: ItemsIntent
    ): Observable<(ListViewState) -> ListViewState> {
        return when (intent) {
            is ItemsIntent.Refresh -> loadPosts(firstId = intent.sinceId)
            is ItemsIntent.LoadMoreItems -> loadPosts(lastId = intent.lastId)
        }
    }

    private fun loadPosts(firstId: String? = null, lastId: String? = null): Observable<(ListViewState) -> ListViewState> {
        fun getOnErrorReducer(): (ListViewState) -> ListViewState =
            { oldState: ListViewState -> oldState.copy(isLoading = false, hasError = true)
            }

        fun getOnSubmitRequestReducer(): (ListViewState) -> ListViewState =
            { oldState: ListViewState -> oldState.copy(isLoading = true)
            }

        fun getOnSuccessReducer(result: List<Item>): (ListViewState) -> ListViewState =
            { oldState ->
                oldState.copy(itemsViewState = firstId?.let {
                    result.map { mapper.transform(it)}.plus(oldState.itemsViewState)
                } ?: lastId?.let {
                    oldState.itemsViewState.plus(result.map { mapper.transform(it) })
                } ?: result.map { mapper.transform(it) }, hasError = false, isLoading = false)
            }

        return getItems.execute(firstId, lastId)
            .observeOn(AndroidSchedulers.mainThread())
            .map(::getOnSuccessReducer)
            .onErrorReturn { getOnErrorReducer() }
            .startWith(getOnSubmitRequestReducer())
    }
}