package fernandocs.domain

import javax.inject.Inject

open class GetItems @Inject constructor(
    private val repository: ItemRepository
) {
    open fun execute(sinceId: String? = null, maxId: String? = null) = repository.getItems(sinceId, maxId)
}
