package com.tada.utils


import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.tada.network.Resource
import com.tada.network.Error

import retrofit2.Call


abstract class NetworkBoundResource<T>(private val appExecutors: AppExecutors) {

    private val result = MediatorLiveData<Resource<T>>()

    init {
        result.value = Resource.loading(null)
        val dbSource = loadFromDatabase()
        result.addSource(dbSource) { data ->

            result.removeSource(dbSource)
            if (shouldLoadFromNetwork(data)) {
                fetchFromNetwork(dbSource)
            } else {

                result.addSource(dbSource) { newData -> result.setValue(Resource.success(newData)) }
            }


        }

    }

    private fun fetchFromNetwork(dbSource: LiveData<T>) {

        appExecutors.networkIO().execute {

            try {
                val response = createNetworkCall().execute()
                //i(TAG, "SUCESS CALLEED 4")
                when (response.isSuccessful) {

                    true -> appExecutors.diskIO().execute {
                        //i(TAG, "SUCESS CALLEED 3")
                        saveNetworkCallResult(response.body())

                        appExecutors.mainThread().execute {
                          //  i(TAG, "SUCESS CALLEED 2")
                            val newDbSource = loadFromDatabase()
                            result.addSource(newDbSource) { newData ->
                                result.removeSource(newDbSource)
                              //  i(TAG, "SUCESS CALLEED")
                                result.setValue(Resource.success(newData))
                            }
                        }
                    }

                    false -> appExecutors.mainThread().execute {
                        result.addSource(dbSource)
                        { newData ->
                            result.setValue(
                                Resource.error(
                                    newData,
                                    Error(response.code(), response.message())
                                )
                            )
                        }
                    }

                }
            } catch (exc: Throwable) {
                appExecutors.mainThread().execute {
                    result.addSource(dbSource) { newData ->
                        result.setValue(
                            Resource.error(
                                newData,
                                Error(503, "Service Unavailable.")
                            )
                        )
                    }
                }
            }
        }
    }

    fun asLiveData(): LiveData<Resource<T>> = result

    @WorkerThread
    protected abstract fun saveNetworkCallResult(data: T?)

    @MainThread
    protected abstract fun shouldLoadFromNetwork(data: T?): Boolean

    @MainThread
    protected abstract fun loadFromDatabase(): LiveData<T>

    @WorkerThread
    protected abstract fun createNetworkCall(): Call<T>
}