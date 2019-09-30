package fernandocs.data.item

import fernandocs.data.item.dto.ItemResponse
import fernandocs.data.item.local.ItemDao
import fernandocs.data.item.local.ItemLocal
import fernandocs.domain.Item
import fernandocs.domain.ItemRepository
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

internal class ItemRepositoryImpl @Inject constructor(
    private val service: ItemService,
    private val dao: ItemDao
) : ItemRepository {

    override fun getItems(sinceId: String?, maxId: String?): Observable<List<Item>> {
        return Observable.concatArray(
            if (sinceId == null && maxId == null) getItemsFromDb() else Observable.empty(),
            getItemsFromApi(sinceId, maxId))
    }

    private fun getItemsFromDb(): Observable<List<Item>> = dao.all
        .toObservable()
        .map { it.map(ItemLocal::mapToDomain) }
        .subscribeOn(Schedulers.io())

    private fun getItemsFromApi(sinceId: String?, maxId: String?) =
        service.getItems(sinceId, maxId)
            .doOnNext { items ->
                if (sinceId == null && maxId == null)
                    dao.insertAll(items.map { it.mapToLocal() })
            }
            .map { it.map(ItemResponse::mapToDomain) }
            .delay(1, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
}
