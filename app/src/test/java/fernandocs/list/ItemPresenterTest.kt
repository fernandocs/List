package fernandocs.list

import fernandocs.domain.GetItems
import fernandocs.domain.Item
import fernandocs.list.item.*
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import io.reactivex.subjects.PublishSubject
import org.junit.After
import org.junit.Before
import org.junit.Test

class ItemPresenterTest {
    private val testSchedulerConfigurator = TestSchedulerConfigurator()
    private val testScheduler = testSchedulerConfigurator.scheduler

    @Test
    fun `Screen is on initialization`() {
        testObserver = presenter.getStates(ListViewState()).test()

        // Then
        testScheduler.triggerActions()
        testObserver
            .assertValueAt(0) { state -> state.isInitial}
        testObserver
            .assertValueAt(1) { state -> state.isLoading }
        testObserver
            .assertValueAt(2) { state ->
                state.itemsViewState.isNotEmpty() && state.itemsViewState.size == 2
            }
    }

    private lateinit var intentsSubject: PublishSubject<ItemsIntent>
    private lateinit var testObserver: TestObserver<ListViewState>
    private lateinit var presenter: ItemsPresenter
    private lateinit var view: ItemsView
    private lateinit var getItems: GetItems
    private lateinit var mapper: ItemViewStateMapper

    @Before
    fun beforeTest() {
        testSchedulerConfigurator.setSchedulers()

        intentsSubject = PublishSubject.create()

        view = mockk(relaxed = true) {
            every { getIntents() } returns intentsSubject
        }

        getItems = mockk {
            every { execute() } returns Observable.just(listOf(item1, item2))
        }

        mapper = mockk(relaxed = true)

        presenter = ItemsPresenter(getItems, mapper, view)
    }

    @After
    fun afterTest() {
        testSchedulerConfigurator.resetSchedulers()
        testObserver.dispose()
    }

    companion object {
        val item1 = Item(
            id = "1",
            text = "title",
            image = "body",
            confidence = 1.5
        )
        val item2 = Item(
            id = "2",
            text = "title",
            image = "body",
            confidence = 1.5
        )
    }
}
