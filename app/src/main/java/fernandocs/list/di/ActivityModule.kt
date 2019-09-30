package fernandocs.list.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import fernandocs.list.MainActivity

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector(modules = [FragmentModule::class])
    abstract fun contributeMainActivity(): MainActivity
}