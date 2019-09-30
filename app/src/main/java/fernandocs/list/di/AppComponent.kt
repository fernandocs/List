package fernandocs.list.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import fernandocs.data.di.DataModule
import fernandocs.list.App
import javax.inject.Named
import javax.inject.Singleton

@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        DataModule::class,
        AppModule::class,
        ActivityModule::class,
        FragmentModule::class
    ]
)
@Singleton
interface AppComponent {

    fun inject(instance: App)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        @BindsInstance
        fun baseUrl(@Named("BASE_URL") url: String): Builder

        @BindsInstance
        fun authorization(@Named("AUTHORIZATION") token: String): Builder

        @BindsInstance
        fun host(@Named("HOST") host: String): Builder

        @BindsInstance
        fun pin(@Named("PIN") pin: String): Builder

        fun build(): AppComponent
    }
}
