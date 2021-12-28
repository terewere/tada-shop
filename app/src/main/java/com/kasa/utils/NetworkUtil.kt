package com.kasa.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.kasa.models.ApiError
import com.kasa.network.RetrofitBuilder
import okhttp3.ResponseBody
import retrofit2.Converter
import java.io.IOException

object NetworkUtil {

    @SuppressLint("MissingPermission")
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val nw = connectivityManager.activeNetwork ?: return false
            val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
            return when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                //for other device how are able to connect with Ethernet
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                //for check internet over Bluetooth
//                actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
                else -> false
            }
        } else {
            val nwInfo = connectivityManager.activeNetworkInfo ?: return false
            return nwInfo.isConnected
        }
    }


    fun convertErrors(response: ResponseBody): ApiError? {
        val retrofitBuilder = RetrofitBuilder()
        val converter: Converter<ResponseBody, ApiError> =
            retrofitBuilder.retrofit.responseBodyConverter(
                ApiError::class.java, arrayOfNulls<Annotation>(0)
            )
        var apiError: ApiError? = null
        try {
            apiError = converter.convert(response)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return apiError
    }


}