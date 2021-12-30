package com.kasa.account

import com.kasa.network.ApiService
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