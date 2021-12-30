package com.tada.account

import com.tada.network.ApiService
import javax.inject.Inject
import javax.inject.Named

class OrderRepository
@Inject
constructor(
    @Named("auth")
    val apiService: ApiService,
) {

   suspend fun getOrders() =  apiService.getOrders()



}