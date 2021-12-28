package com.kasa.products

import android.util.Log.i
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.kasa.databases.DB
import com.kasa.models.ProductWithImages
import com.kasa.network.ApiService
import com.kasa.paging3.RemoteKeys
import com.kasa.products.ProductRepository.Companion.DEFAULT_PAGE_INDEX
import retrofit2.HttpException
import java.io.IOException


@ExperimentalPagingApi
class ProductMediator
     constructor(
        private val apiService: ApiService,
        private val appDatabase: DB,
        private val categoryId: Int,
        ) :
    RemoteMediator<Int, ProductWithImages>() {

    override suspend fun initialize(): InitializeAction {
        // Require that remote REFRESH is launched on initial load and succeeds before launching
        // remote PREPEND / APPEND.
        i("testing", "REFFRESHHHH");
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType, state: PagingState<Int, ProductWithImages>
    ): MediatorResult {

        val pageKeyData = getKeyPageData(loadType, state)
        val page = when (pageKeyData) {
            is MediatorResult.Success -> {
                return pageKeyData
            }
            else -> {
                pageKeyData as Int
            }
        }

        try {
            val response = apiService.getProducts( state.config.pageSize,categoryId, page)

           // i("testing", response.toString());
//            i("testing", response.size.toString());
//            i("testing", response[0].item.label);
//            val isEndOfList = response.isEmpty()
            val isEndOfList = response.size < state.config.pageSize
            i("testing",  "isEndOfList: ${isEndOfList} ")

            appDatabase.withTransaction {

                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    appDatabase.getRepoDao().clearRemoteKeys()
//                    appDatabase.productDao().empty()
                    appDatabase.productDao().clearByCatId(categoryId)
                    i("testing",  " loadType == LoadType.REFRESH ");

                }
                val prevKey = if (page == DEFAULT_PAGE_INDEX) null else page - 1
                val nextKey = if (isEndOfList) null else page + 1

//               i("testing", nextKey.toString() + " nextKey");
//               i("testing", prevKey.toString() + " prevKey");

                val keys = response.map {
                    RemoteKeys( productId = it.product.id, prevKey = prevKey, nextKey = nextKey)
                }
                appDatabase.getRepoDao().insertAll(keys)
                appDatabase.productDao().insertProducts(response, keys)
            }
            return MediatorResult.Success(endOfPaginationReached = isEndOfList)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    /**
     * this returns the page key or the final end of list success result
     */
    suspend fun getKeyPageData(loadType: LoadType, state: PagingState<Int, ProductWithImages>): Any? {
        return when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getClosestRemoteKey(state)
                remoteKeys?.nextKey?.minus(1) ?: DEFAULT_PAGE_INDEX
            }
            LoadType.APPEND -> {
                val remoteKeys = getLastRemoteKey(state)
                    ?:  return MediatorResult.Success(
                        endOfPaginationReached = true
                    )
                remoteKeys.nextKey
            }
            LoadType.PREPEND -> {

                val remoteKeys = getFirstRemoteKey(state)
                    ?:  return MediatorResult.Success(
                        endOfPaginationReached = true
                    )
                //end of list condition reached
                remoteKeys.prevKey ?: return MediatorResult.Success(endOfPaginationReached = true)
                remoteKeys.prevKey
            }
        }
    }

    /**
     * get the last remote key inserted which had the data
     */
    private suspend fun getLastRemoteKey(state: PagingState<Int, ProductWithImages>): RemoteKeys? {

        i("testing", " getLastRemoteKey");
        i("testing", state.pages.size.toString()+ " size");

        return state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { doggo -> appDatabase.getRepoDao().remoteKeysDoggoId(doggo.product.id) }
    }

    /**
     * get the first remote key inserted which had the data
     */
    private suspend fun getFirstRemoteKey(state: PagingState<Int, ProductWithImages>): RemoteKeys? {

        i("testing", " getFirstRemoteKey");

        return state.pages
            .firstOrNull() { it.data.isNotEmpty() }
            ?.data?.firstOrNull()
            ?.let { doggo -> appDatabase.getRepoDao().remoteKeysDoggoId(doggo.product.id) }
    }

    /**
     * get the closest remote key inserted which had the data
     */
    private suspend fun getClosestRemoteKey(state: PagingState<Int, ProductWithImages>): RemoteKeys? {

        i("testing", " getClosestRemoteKey");
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.product?.id.let { productId ->
                appDatabase.getRepoDao().remoteKeysDoggoId(productId)
            }
        }
    }

}