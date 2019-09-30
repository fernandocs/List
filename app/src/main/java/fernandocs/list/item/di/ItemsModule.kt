package fernandocs.list.item.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import fernandocs.list.di.ViewModelKey
import fernandocs.list.item.*

@Module(includes = [ItemsModule.ProvideViewModel::class])
abstract class ItemsModule {

    @ContributesAndroidInjector(modules = [InjectViewModel::class])
    abstract fun contributeItemsFragment(): ItemsFragment

    @Binds
    abstract fun bindHItemsView(fragment: ItemsFragment): ItemsView

    @Module
    class ProvideViewModel {

        @Provides
        @IntoMap
        @ViewModelKey(ItemsViewModel::class)
        fun provideItemsViewModel(): ViewModel = ItemsViewModel()
    }

    @Module
    class InjectViewModel {

        @Provides
        fun provideItemsViewModel(
            factory: ViewModelProvider.Factory,
            target: ItemsFragment
        ): ItemsViewModel =
            ViewModelProviders.of(target, factory).get(ItemsViewModel::class.java)
    }
}
