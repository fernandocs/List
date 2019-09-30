package fernandocs.list.item

import io.reactivex.Observable

interface ItemsView {
    fun getIntents(): Observable<ItemsIntent>
}
