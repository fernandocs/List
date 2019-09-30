package fernandocs.data.item.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ItemLocal::class], version = 1)
abstract class ItemsDatabase : RoomDatabase() {
    abstract val itemDao: ItemDao
}
