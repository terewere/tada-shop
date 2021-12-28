//package com.kasa.paging3.data
//
//import android.util.Log.i
//import androidx.paging.ExperimentalPagingApi
//import androidx.paging.LoadType
//import androidx.paging.PagingState
//import androidx.paging.RemoteMediator
//import androidx.room.withTransaction
//import com.kasa.databases.DB
//import com.kasa.models.ItemWithImagesAndCategory
//import com.kasa.network.ApiService
//import com.kasa.paging3.data.DoggoImagesRepository.Companion.DEFAULT_PAGE_INDEX
//import com.kasa.paging3.RemoteKeys
//import retrofit2.HttpException
//import java.io.IOException
//import javax.inject.Inject
//
//
//@ExperimentalPagingApi
//class DoggoMediator
//    @Inject constructor(val doggoApiService: ApiService, val appDatabase: DB) :
//    RemoteMediator<Int, ItemWithImagesAndCategory>() {
//
//    override suspend fun initialize(): InitializeAction {
//        // Require that remote REFRESH is launched on initial load and succeeds before launching
//        // remote PREPEND / APPEND.
//        return InitializeAction.LAUNCH_INITIAL_REFRESH
//    }
//
//    override suspend fun load(
//        loadType: LoadType, state: PagingState<Int, ItemWithImagesAndCategory>
//    ): MediatorResult {
//
//        val pageKeyData = getKeyPageData(loadType, state)
//        val page = when (pageKeyData) {
//            is MediatorResult.Success -> {
//                return pageKeyData
//            }
//            else -> {
//                pageKeyData as Int
//            }
//        }
//
//        try {
//            val response = doggoApiService.getProducts(state.config.pageSize, page)
//
////            i("testing", response.toString());
////            i("testing", response.size.toString());
////            i("testing", response[0].item.label);
//            val isEndOfList = response.isEmpty()
//
//            appDatabase.withTransaction {
//
//                // clear all tables in the database
//                if (loadType == LoadType.REFRESH) {
////                    appDatabase.getRepoDao().clearRemoteKeys()
////                    appDatabase.messageDao().empty()
//                    appDatabase.messageDao().clear()
//                }
//                val prevKey = if (page == DEFAULT_PAGE_INDEX) null else page - 1
//                val nextKey = if (isEndOfList) null else page + 1
//
//               i("testing", nextKey.toString() + " nextKey");
//               i("testing", prevKey.toString() + " prevKey");
//
//                val keys = response.map {
//                    RemoteKeys(repoId = it.item.id, prevKey = prevKey, nextKey = nextKey)
//                }
////                appDatabase.getRepoDao().insertAll(keys)
//                appDatabase.messageDao().insertCategoryWithItems(response, keys)
//            }
//            return MediatorResult.Success(endOfPaginationReached = isEndOfList)
//        } catch (exception: IOException) {
//            return MediatorResult.Error(exception)
//        } catch (exception: HttpException) {
//            return MediatorResult.Error(exception)
//        }
//    }
//
//    /**
//     * this returns the page key or the final end of list success result
//     */
//    suspend fun getKeyPageData(loadType: LoadType, state: PagingState<Int, ItemWithImagesAndCategory>): Any? {
//        return when (loadType) {
//            LoadType.REFRESH -> {
//                val remoteKeys = getClosestRemoteKey(state)
//                remoteKeys?.nextKey?.minus(1) ?: DEFAULT_PAGE_INDEX
//            }
//            LoadType.APPEND -> {
//                val remoteKeys = getLastRemoteKey(state)
//                    ?:  return MediatorResult.Success(
//                        endOfPaginationReached = true
//                    )
//                remoteKeys.nextKey
//            }
//            LoadType.PREPEND -> {
//
//                val remoteKeys = getFirstRemoteKey(state)
//                    ?:  return MediatorResult.Success(
//                        endOfPaginationReached = true
//                    )
//                //end of list condition reached
//                remoteKeys.prevKey ?: return MediatorResult.Success(endOfPaginationReached = true)
//                remoteKeys.prevKey
//            }
//        }
//    }
//
//    /**
//     * get the last remote key inserted which had the data
//     */
//    private suspend fun getLastRemoteKey(state: PagingState<Int, ItemWithImagesAndCategory>): RemoteKeys? {
//
//        i("testing", " getLastRemoteKey");
//        i("testing", state.pages.size.toString()+ " size");
//
//        return state.pages
//            .lastOrNull { it.data.isNotEmpty() }
//            ?.data?.lastOrNull()
//            ?.let { doggo -> appDatabase.getRepoDao().remoteKeysDoggoId(doggo.item.id) }
//    }
//
//    /**
//     * get the first remote key inserted which had the data
//     */
//    private suspend fun getFirstRemoteKey(state: PagingState<Int, ItemWithImagesAndCategory>): RemoteKeys? {
//
//        i("testing", " getFirstRemoteKey");
//
//        return state.pages
//            .firstOrNull() { it.data.isNotEmpty() }
//            ?.data?.firstOrNull()
//            ?.let { doggo -> appDatabase.getRepoDao().remoteKeysDoggoId(doggo.item.id) }
//    }
//
//    /**
//     * get the closest remote key inserted which had the data
//     */
//    private suspend fun getClosestRemoteKey(state: PagingState<Int, ItemWithImagesAndCategory>): RemoteKeys? {
//
//        i("testing", " getClosestRemoteKey");
//        return state.anchorPosition?.let { position ->
//            state.closestItemToPosition(position)?.item?.id.let { repoId ->
//                appDatabase.getRepoDao().remoteKeysDoggoId(repoId)
//            }
//        }
//    }
//
//}