package fernandocs.domain

import io.reactivex.Observable

interface ItemRepository {
    fun getItems(sinceId: String?, maxId: String?): Observable<List<Item>>
}
