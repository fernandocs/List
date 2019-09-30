package fernandocs.data.item.local

import androidx.room.*
import io.reactivex.Single

@Dao
interface ItemDao {
    @get:Query("SELECT * FROM items")
    val all: Single<List<ItemLocal>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(users: List<ItemLocal>)
}
