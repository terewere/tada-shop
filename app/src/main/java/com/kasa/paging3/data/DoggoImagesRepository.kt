//package com.kasa.paging3.data
//
//import android.content.Context
//import android.util.Log.i
//import androidx.lifecycle.LiveData
//import androidx.paging.*
//import com.kasa.databases.DB
//import com.kasa.models.Category
//import com.kasa.models.ItemWithImagesAndCategory
//import com.kasa.network.ApiService
//import com.kasa.utils.AppExecutors
//import com.kasa.utils.Constants.TAG
//import com.kasa.utils.NetworkBoundResource
//import com.kasa.utils.NetworkUtil
//import kotlinx.coroutines.flow.Flow
//import retrofit2.Call
//import javax.inject.Inject
//import javax.inject.Named
//
//
///**
// * repository class to manage the data flow and map it if needed
// */
//@ExperimentalPagingApi
//class DoggoImagesRepository
//@Inject
//constructor(
//    @Named("auth")
//    val doggoApiService: ApiService,
//    val appDatabase: DB,
//    val context: Context,
//    val appExecutors: AppExecutors
//) {
//
//    companion object {
//        const val DEFAULT_PAGE_INDEX = 1
//        const val DEFAULT_PAGE_SIZE = 50
//
//        //get doggo repository instance
////        fun getInstance() = DoggoImagesRepository()
//    }
//
//
//    fun letDoggoImagesFlow(pagingConfig: PagingConfig = getDefaultPageConfig()): Flow<PagingData<ItemWithImagesAndCategory>> {
//        return Pager(
//            config = pagingConfig,
//            pagingSourceFactory = { DoggoImagePagingSource(doggoApiService) }
//        ).flow
//    }
//
//
//    /**
//     * let's define page size, page size is the only required param, rest is optional
//     */
//    fun getDefaultPageConfig(): PagingConfig {
//        return PagingConfig(
//            pageSize = DEFAULT_PAGE_SIZE,
//            enablePlaceholders = true,
////         maxSize = 400,
////         initialLoadSize = 150
//        )
//    }
//
//    fun letDoggoImagesFlowDb(): Flow<PagingData<ItemWithImagesAndCategory>> {
//
//        val pagingSourceFactory = { appDatabase.messageDao().getCategoryWithItemsWithImages2() }
//
//
//        return Pager(
//            config = getDefaultPageConfig(),
//            remoteMediator = DoggoMediator(doggoApiService, appDatabase),
//            pagingSourceFactory = pagingSourceFactory
//
//        ).flow
//    }
//
//
//
//}