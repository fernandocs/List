package fernandocs.data

import fernandocs.data.item.ItemRepositoryImpl
import fernandocs.data.item.ItemService
import fernandocs.data.item.dto.ItemResponse
import fernandocs.data.item.local.ItemDao
import fernandocs.data.item.local.ItemLocal
import fernandocs.domain.Item
import fernandocs.domain.ItemRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Observable
import io.reactivex.Single
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldStartWith
import org.junit.Before
import org.junit.Test

class ItemRepositoryImplTest {
    @Test
    fun `Get More Items - Sending maxId - Returns list of items`() {
        // Given
        val itemResponse = mockk<ItemResponse>(relaxed = true)
        val itemsLocal = mockk<ItemLocal>(relaxed = true)

        every { service.getItems(any(), any()) } returns Observable.just(listOf(itemResponse))
        every { dao.all } returns Single.just(listOf(itemsLocal))

        val itemsResult = mockk<Item>()

        every { itemResponse.mapToDomain() } returns itemsResult
        var thread = ""

        // When
        val result = Single.fromObservable(repository.getItems(null, "id" ))
            .doOnEvent { _, _ -> thread = Thread.currentThread().name }
            .blockingGet()

        // Then
        verify { service.getItems(null, "id") }
        verify { itemResponse.mapToDomain() }
        result shouldEqual listOf(itemsResult)
        thread shouldStartWith "RxCachedThreadScheduler"
    }

    private lateinit var service: ItemService
    private lateinit var repository: ItemRepository
    private lateinit var dao: ItemDao

    @Before
    fun setup() {
        service = mockk()
        dao = mockk(relaxed = true)
        repository = ItemRepositoryImpl(service, dao)
    }
}