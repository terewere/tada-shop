package com.kasa.auth

import android.Manifest
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import com.kasa.network.ApiService
import io.reactivex.disposables.Disposable
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    @Named("auth") private val apiService: ApiService
) {

    var disposable: Disposable? = null

    fun verifyPhone(number: String) = apiService.verifyPhone(number)
    fun verifyToken(number: String, token: String) = apiService.verifyToken(number, token)
    fun updateUser(firstName: String, lastName: String) = apiService.updateUser(firstName, lastName)

//    fun saveToken(deviceId: String, token: String) = apiService.saveToken(deviceId = deviceId, token = token )


}