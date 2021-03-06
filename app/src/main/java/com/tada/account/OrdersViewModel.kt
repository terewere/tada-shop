package com.tada.account

import android.util.Log.i
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tada.auth.ApiResult
import com.tada.models.Order
import com.tada.utils.Constants.TAG
import kotlinx.coroutines.launch
import javax.inject.Inject

class OrdersViewModel
@Inject constructor(
    private val repository: OrderRepository
) : ViewModel() {

    private val _ordersResult = MutableLiveData<ApiResult<List<Order>>>()
    var orders: LiveData<ApiResult<List<Order>>> = _ordersResult

    fun getOrders() {

        viewModelScope.launch {
            try {

                val response = repository.getOrders()

                i(TAG, response.toString())

                _ordersResult.postValue(ApiResult(success = response ))

            } catch (e: Throwable) {
                _ordersResult.postValue(ApiResult())
               i(TAG, e.localizedMessage)

            }
        }
    }





}
