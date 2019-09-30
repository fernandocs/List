package fernandocs.list.item

import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import fernandocs.list.R
import kotlinx.android.synthetic.main.item.*
import android.graphics.BitmapFactory
import android.util.Base64

data class ListItem(val viewState: ItemViewState) : Item() {
    override fun getLayout() = R.layout.item

    override fun bind(viewHolder: ViewHolder, position: Int) {
        with(viewHolder) {
            textViewId.text = viewState.id
            textViewText.text = viewState.text
            val decodedString = Base64.decode(viewState.imageBase64, Base64.DEFAULT)
            val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
            imageViewImage.setImageBitmap(decodedByte)
            textViewSecret.text = viewState.secret
        }
    }

    override fun isSameAs(other: com.xwray.groupie.Item<*>?) =
        other is ListItem && this.viewState.id == other.viewState.id

    override fun equals(other: Any?) = other is ListItem && this.viewState.id == other.viewState.id
}
