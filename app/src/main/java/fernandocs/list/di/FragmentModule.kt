package fernandocs.list.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import fernandocs.list.AppViewModelFactory
import fernandocs.list.item.di.ItemsModule
import javax.inject.Provider

@Module(includes = [ItemsModule::class])
class FragmentModule {

    @Provides
    fun provideViewModelFactory(
        providers: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
    ): ViewModelProvider.Factory = AppViewModelFactory(providers)
}
