package com.kasa.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.kasa.network.TokenManager
import com.kasa.utils.AppExecutors
import com.kasa.utils.Constants.SUCCESS
import com.kasa.utils.Constants.TAG
import com.kasa.utils.NetworkUtil
import javax.inject.Inject

class UpdateProfileViewModel
@Inject constructor(
    private val repository: AuthRepository,
    val appExecutors: AppExecutors,
    val tokenManager: TokenManager
) : ViewModel() {

    private val _loginResult = MutableLiveData<ApiResult<String>>()
    var apiResult: LiveData<ApiResult<String>> = _loginResult

    fun updateUser(firstName: String, lastName: String) {
        appExecutors.networkIO().execute {
            try {

                val response =
                    repository.updateUser(firstName, lastName).execute()
                if (response.isSuccessful) {

                    response.body()?.let {
                        tokenManager.user = it
                    }
                    _loginResult.postValue(ApiResult(success = SUCCESS))

                } else {
                    response.errorBody()?.let {
                        val convertErrors = NetworkUtil.convertErrors(it)
                        _loginResult.postValue(ApiResult(error = convertErrors))

                    }

                }
            } catch (e: Throwable) {
                _loginResult.postValue(ApiResult())
               Log.i(TAG, e.localizedMessage)

            }
        }
    }





}
