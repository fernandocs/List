package fernandocs.list.item

sealed class ItemsIntent {
    data class Refresh(val sinceId: String): ItemsIntent()
    data class LoadMoreItems(val lastId: String): ItemsIntent()
}