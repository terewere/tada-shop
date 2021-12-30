package com.tada.auth

import android.util.Log.i
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tada.network.TokenManager
import com.tada.utils.Constants.SUCCESS
import com.tada.utils.Constants.TAG
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthViewModel
@Inject constructor(
    private val repository: AuthRepository,
    val tokenManager: TokenManager
) : ViewModel() {

    private val _loginResult = MutableLiveData<ApiResult<String>>()
    var apiResult: LiveData<ApiResult<String>> = _loginResult

    fun register(firstName: String, lastName: String, email: String, phone: String) {

        i(TAG, firstName)
        i(TAG, lastName)
        i(TAG, email)
        i(TAG, phone)

        viewModelScope.launch {
            try {

                val response = repository.register(firstName, lastName, email, phone)

                i(TAG, response.toString())
                tokenManager.user = response.user
                tokenManager.accessToken = response.accessToken

                _loginResult.postValue(ApiResult(success = SUCCESS))

            } catch (e: Throwable) {
                _loginResult.postValue(ApiResult())
               i(TAG, e.localizedMessage)

            }
        }
    }
    fun login(phone: String) {
        viewModelScope.launch {
            try {

                val response = repository.login( phone)

                    tokenManager.user = response.user
                    tokenManager.accessToken = response.accessToken

                _loginResult.postValue(ApiResult(success = SUCCESS))

            } catch (e: Throwable) {
                _loginResult.postValue(ApiResult())
               i(TAG, e.localizedMessage)

            }
        }
    }





}
