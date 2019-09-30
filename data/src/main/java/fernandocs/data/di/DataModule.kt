package fernandocs.data.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import fernandocs.data.ApiClientFactory
import fernandocs.data.BuildConfig
import fernandocs.data.item.di.ItemModule
import fernandocs.data.item.local.ItemsDatabase
import okhttp3.CertificatePinner
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named

@Module(
    includes = [ItemModule::class]
)
class DataModule {
    @Provides
    internal fun provideApiClientFactory(
        @Named("BASE_URL") url: String,
        @Named("AUTHORIZATION") token: String,
        @Named("HOST") host: String,
        @Named("PIN") pin: String
    ): ApiClientFactory {
        val interceptor = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }
        val certificatePinner = CertificatePinner.Builder()
            .add(host, pin)
            .build()

        val okHttp = OkHttpClient.Builder()
            .certificatePinner(certificatePinner)
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .addInterceptor(HeaderInterceptor(token))
            .build()

        return ApiClientFactory(
            Retrofit.Builder()
                .baseUrl(url)
                .client(okHttp)
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        )
    }

    @Provides
    fun providesItemsDatabase(context: Context): ItemsDatabase {
        return Room.databaseBuilder(context, ItemsDatabase::class.java, "items-db").build()
    }
}

class HeaderInterceptor(private val token: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = chain.run {
        proceed(
            request()
                .newBuilder()
                .addHeader("Authorization", token)
                .build()
        )
    }
}
