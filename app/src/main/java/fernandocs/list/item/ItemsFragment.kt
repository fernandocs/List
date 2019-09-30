package fernandocs.list.item

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import dagger.android.support.AndroidSupportInjection
import fernandocs.list.R
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.fragment_list.view.*
import javax.inject.Inject

class ItemsFragment : Fragment(), ItemsView {

    @Inject lateinit var presenter: ItemsPresenter
    @Inject lateinit var viewModel: ItemsViewModel

    private val disposable = CompositeDisposable()
    private val adapter = GroupAdapter<ViewHolder>()
    private val intents = PublishSubject.create<ItemsIntent>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(view) {
            viewItemContent.apply {
                layoutManager = LinearLayoutManager(activity)
                adapter = this@ItemsFragment.adapter
            }

            viewItemContent.addOnScrollListener(object : RecyclerView.OnScrollListener() {

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (!recyclerView.canScrollVertically(1)) {
                        intents.onNext(ItemsIntent.LoadMoreItems(viewModel.currentViewState.itemsViewState.last().id))

                    }
                    if (!recyclerView.canScrollVertically(-1)) {
                        intents.onNext(ItemsIntent.Refresh(viewModel.currentViewState.itemsViewState.first().id))
                    }
                }
            })
        }
    }

    override fun onStart() {
        super.onStart()
        presenter.getStates(viewModel.currentViewState)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::render)
            .addTo(disposable)
    }

    override fun onStop() {
        disposable.clear()
        super.onStop()
    }

    override fun getIntents(): Observable<ItemsIntent> = intents

    private fun render(viewState: ListViewState) {
        viewModel.currentViewState = viewState

        viewItemsLoading.visibility = if (viewState.isLoading) View.VISIBLE else View.GONE

        adapter.update(viewState.itemsViewState.map(::ListItem))

        if (viewState.hasError) {
            Toast.makeText(activity, R.string.error_message, Toast.LENGTH_SHORT).show()
        }
    }
}
