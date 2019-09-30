package fernandocs.data.item.dto

import fernandocs.data.item.local.ItemLocal
import fernandocs.domain.Item

internal data class ItemResponse(
    val _id: String,
    val text: String,
    val img: String,
    val confidence: Double
) {
    internal fun mapToDomain() = Item(
        id = _id,
        text = text,
        image = img,
        confidence = confidence
    )

    internal fun mapToLocal() = ItemLocal(
        id = _id,
        text = text,
        image = img,
        confidence = confidence
    )
}
