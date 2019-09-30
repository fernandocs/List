package fernandocs.list.item


import android.os.Parcelable
import androidx.lifecycle.ViewModel
import fernandocs.domain.Item
import kotlinx.android.parcel.Parcelize
import javax.inject.Inject

class ItemsViewModel @Inject constructor() : ViewModel() {
    var currentViewState: ListViewState = ListViewState()
}

@Parcelize
data class ListViewState(
    val isInitial: Boolean = true,
    val isLoading: Boolean = false,
    val hasError: Boolean = false,
    val itemsViewState: List<ItemViewState> = emptyList()
) : Parcelable

@Parcelize
data class ItemViewState(
    val id: String,
    val text: String,
    val imageBase64: String,
    val secret: String
) : Parcelable

open class ItemViewStateMapper @Inject constructor() {
    open fun transform(item: Item) = ItemViewState(
        id = item.id,
        text = item.text,
        imageBase64 = item.image,
        secret = item.confidence.toString()
    )
}
