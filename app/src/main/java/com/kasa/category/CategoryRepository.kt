package com.kasa.category

import android.content.Context
import androidx.lifecycle.LiveData
import com.kasa.databases.DB
import com.kasa.models.Category
import com.kasa.network.ApiService
import com.kasa.utils.AppExecutors
import com.kasa.utils.NetworkBoundResource
import com.kasa.utils.NetworkUtil
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