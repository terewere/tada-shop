package com.tada.auth

import com.tada.network.ApiService
import io.reactivex.disposables.Disposable
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    @Named("auth") private val apiService: ApiService
) {

    var disposable: Disposable? = null

    suspend fun register(firstName: String, lastName: String, email: String, phone: String) =
        apiService.register(firstName, lastName, email,phone)

   suspend fun login(phone: String) = apiService.login(phone)

}