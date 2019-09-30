package fernandocs.data.item.di

import dagger.Module
import dagger.Provides
import fernandocs.data.ApiClientFactory
import fernandocs.data.item.ItemRepositoryImpl
import fernandocs.data.item.ItemService
import fernandocs.data.item.local.ItemDao
import fernandocs.data.item.local.ItemsDatabase
import fernandocs.domain.ItemRepository
import javax.inject.Singleton

@Module
internal class ItemModule {
    @Provides
    fun providesItemDao(demoDatabase: ItemsDatabase): ItemDao {
        return demoDatabase.itemDao
    }

    @Provides
    fun provideItemRepository(service: ItemService, dao: ItemDao): ItemRepository {
        return ItemRepositoryImpl(service, dao)
    }

    @Provides
    internal fun provideItemService(apiClientFactory: ApiClientFactory): ItemService {
        return apiClientFactory.retrofit.create(ItemService::class.java)
    }
}
