package fernandocs.list

import android.app.Activity
import android.app.Application
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import fernandocs.list.di.DaggerAppComponent
import javax.inject.Inject

class App : Application(), HasActivityInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun activityInjector() = dispatchingAndroidInjector

    override fun onCreate() {
        super.onCreate()
        DaggerAppComponent.builder()
            .application(this)
            .baseUrl(BuildConfig.BASE_URL)
            .authorization(BuildConfig.AUTHORIZATION)
            .host(BuildConfig.HOST)
            .pin(BuildConfig.PIN)
            .build()
            .inject(this)
    }
}