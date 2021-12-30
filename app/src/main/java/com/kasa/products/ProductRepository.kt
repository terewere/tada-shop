package com.kasa.products

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.kasa.databases.DB
import com.kasa.models.ProductWithImages
import com.kasa.network.ApiService
import com.kasa.utils.AppExecutors
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Named


@ExperimentalPagingApi
class ProductRepository
@Inject
constructor(
    @Named("auth")
    val apiService: ApiService,
    val appDatabase: DB,
    val context: Context,
    val appExecutors: AppExecutors
) {

    companion object {
        const val DEFAULT_PAGE_INDEX = 1
        const val DEFAULT_PAGE_SIZE = 50
    }



    fun getDefaultPageConfig(): PagingConfig {
        return PagingConfig(
            pageSize = DEFAULT_PAGE_SIZE,
            enablePlaceholders = true,
//         maxSize = 400,
        // initialLoadSize = 60,
            prefetchDistance = 100
        )
    }

    fun getProducts(categoryId: Int): Flow<PagingData<ProductWithImages>> {

        val pagingSourceFactory = { appDatabase.productDao().getProducts(categoryId) }

        return Pager(
            config = getDefaultPageConfig(),
            remoteMediator = ProductMediator(apiService, appDatabase, categoryId),
            pagingSourceFactory = pagingSourceFactory

        ).flow
    }

    fun getAllProducts(): Flow<PagingData<ProductWithImages>> {

        val pagingSourceFactory = { appDatabase.productDao().getAllProducts() }

        return Pager(
            config = getDefaultPageConfig(),
            remoteMediator = ProductMediator(apiService, appDatabase),
            pagingSourceFactory = pagingSourceFactory

        ).flow
    }

    fun getMessages(): Flow<PagingData<ProductWithImages>> {
        return Pager(
            config = getDefaultPageConfig(),
            pagingSourceFactory = { ProductPagingSource(apiService) }
        ).flow
    }

    suspend fun getHomeSlides() =  apiService.getHomeSlides()



}