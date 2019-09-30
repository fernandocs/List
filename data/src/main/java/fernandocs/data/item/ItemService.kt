package fernandocs.data.item

import fernandocs.data.item.dto.ItemResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

internal interface ItemService {
    @GET("/e/mock/v1/items")
    fun getItems(@Query("since_id") sinceId: String?,
                 @Query("max_id") maxId: String?): Observable<List<ItemResponse>>
}
