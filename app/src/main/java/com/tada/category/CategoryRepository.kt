package com.tada.category

import android.content.Context
import androidx.lifecycle.LiveData
import com.tada.databases.DB
import com.tada.models.Category
import com.tada.network.ApiService
import com.tada.utils.AppExecutors
import com.tada.utils.NetworkBoundResource
import com.tada.utils.NetworkUtil
import retrofit2.Call
import javax.inject.Inject
import javax.inject.Named


class CategoryRepository
@Inject
constructor(
    @Named("auth")
    val apiService: ApiService,
    val appDatabase: DB,
    val context: Context,
    val appExecutors: AppExecutors
) {

    fun getCategories() =
        object : NetworkBoundResource<List<Category>?>(appExecutors) {

            override fun saveNetworkCallResult(data: List<Category>?) {
                appDatabase.categoryDao().insertCategories(data)
            }

            override fun shouldLoadFromNetwork(data: List<Category>?): Boolean {
                return NetworkUtil.isNetworkAvailable(context)
            }

            override fun loadFromDatabase(): LiveData<List<Category>?> {
                return appDatabase.categoryDao().getCategories()
            }

            override fun createNetworkCall(): Call<List<Category>?> {
                return apiService.getCategories()
            }


        }.asLiveData()


}