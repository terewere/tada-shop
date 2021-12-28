package com.kasa.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.kasa.models.ApiError
import com.kasa.network.TokenManager
import com.kasa.utils.AppExecutors
import com.kasa.utils.Constants.SUCCESS
import com.kasa.utils.Constants.TAG
import com.kasa.utils.NetworkUtil
import javax.inject.Inject

class VerifyPhoneViewModel
@Inject constructor(
    private val repository: AuthRepository,
    val appExecutors: AppExecutors
    ) : ViewModel() {

    private var savedState: SavedStateHandle = SavedStateHandle()

    init {
        setInitialDefaultValue<NumberViewState?>(STATE_NUMBER, NumberViewState.INITIAL)
    }


    private val _loginResult = MutableLiveData<ApiResult<String>>()
    var apiResult: LiveData<ApiResult<String>> = _loginResult

    fun verifyPhone(number: String) {

        appExecutors.networkIO().execute {
           try {
               val response = repository.verifyPhone(number).execute()
               if (response.isSuccessful) {
                   _loginResult.postValue(ApiResult(success = response.body()))
               } else {
                   response.errorBody()?.let {
                       val convertErrors = NetworkUtil.convertErrors(it)
                       _loginResult.postValue(ApiResult(error = convertErrors))

                   }

               }
           } catch (e: Throwable){
               val apiError = ApiError()
               apiError.message = "Something went wrong"
               _loginResult.postValue(ApiResult(error = apiError ))
               Log.i(TAG, e.localizedMessage)

           }
        }
    }


    protected fun <T> setInitialDefaultValue(key: String, initialValue: T) {
        if (!savedState.contains(key) || savedState.get<Any>(key) == null) {
            savedState.set(key, initialValue)
        }
    }

    fun getNumber(): NumberViewState {
        return savedState.get<NumberViewState>(STATE_NUMBER) ?:
        return NumberViewState.INITIAL

    }
    fun onNumberDetected(countryCode: Int, nationalNumber: String?) {
        setViewState(
            getNumber().toBuilder()
                .countryCode(countryCode)
                .nationalNumber(nationalNumber)
                .build()
        )
    }
    fun getLiveNumber(): LiveData<NumberViewState?> {
        return savedState.getLiveData<NumberViewState>(STATE_NUMBER)
    }

    fun onCountrySelected(selectedCountryName: String?, countryCode: Int) {
        setViewState(
            getNumber().toBuilder()
                .selectedCountryDisplayName(selectedCountryName)
                .countryCode(countryCode).build()
        )
    }

    fun setNationalNumber(number: String?) {
        val numberViewState = getNumber().toBuilder().nationalNumber(number).build()
        setViewState(numberViewState)
    }

    protected fun setViewState(numberViewState: NumberViewState) {
        if (numberViewState != getNumber()) {
            savedState.set<NumberViewState>(STATE_NUMBER, numberViewState)
        }
    }


    companion object{
         const val STATE_NUMBER = "NUMBER"

    }

}