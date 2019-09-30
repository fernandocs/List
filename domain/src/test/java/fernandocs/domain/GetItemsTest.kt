package fernandocs.domain

import io.mockk.every
import io.mockk.mockk
import io.reactivex.Observable
import org.amshove.kluent.shouldEqual
import org.junit.Test

class GetItemsTest {
    @Test
    fun `GetItems use case returns correct list of items`() {
        val repo = mockk<ItemRepository> {
            every { getItems(any(), any()) } returns Observable.just(items)
        }

        val useCase = GetItems(repo)

        val result = useCase.execute().blockingFirst()

        result shouldEqual items
    }

    companion object {
        val items = listOf(Item(id = "1", text = "text", image = "image", confidence = 1.1))
    }
}