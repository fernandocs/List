package fernandocs.data.item.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import fernandocs.domain.Item

@Entity(tableName = "items")
data class ItemLocal(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "text") val text: String,
    @ColumnInfo(name = "image") val image: String,
    @ColumnInfo(name = "confidence") val confidence: Double
) {
    internal fun mapToDomain() = Item(
        id = id,
        text = text,
        image = image,
        confidence = confidence
    )
}
