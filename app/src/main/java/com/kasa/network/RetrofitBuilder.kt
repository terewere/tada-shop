package com.kasa.network

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.kasa.models.ProductWithImages
import com.kasa.utils.Constants.API_URL
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class RetrofitBuilder @Inject constructor() {

    val retrofit by lazy {
        buildRetrofit()
    }



    fun buildRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(API_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gsonDeserializer()))
            .client(client)
            .build()
    }


    private fun gsonDeserializer(): Gson = GsonBuilder()

        .registerTypeAdapter(object : TypeToken<List<@JvmSuppressWildcards ProductWithImages>>() {}.type,
            Deserializer<ProductWithImages>()
        )
        .setDateFormat("yyyy-MM-dd HH:mm:ss")
        .setLenient()
        .create()

    fun createService(service: Class<ApiService>): ApiService {
        return retrofit.create(service)
    }

    fun createServiceWithAuth(
        service: Class<ApiService>,
        authenticator: CustomAuthenticator
    ): ApiService {
        val newClient =
            client.newBuilder().addInterceptor(object : Interceptor {
                @Throws(IOException::class)
                override fun intercept(chain: Interceptor.Chain): Response {
                    var request = chain.request()
                    val builder = request.newBuilder()
                    builder.addHeader(
                        "Authorization",
                        "Bearer " + authenticator.tokenManager.accessToken
                    )

                    request = builder.build()
                    return chain.proceed(request)
                }
            }).authenticator(authenticator).build()
        val newRetrofit = retrofit.newBuilder().client(newClient).build()
        return newRetrofit.create(service)
    }


    companion object {

        private val client by lazy {

            val logger = HttpLoggingInterceptor()
            logger.level = HttpLoggingInterceptor.Level.BASIC
            val builder = OkHttpClient.Builder()
                .writeTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .addInterceptor { chain ->
                    var request = chain.request()
                    val builder = request.newBuilder()

                        .addHeader("Accept", "application/json")
                        .addHeader("Content-Type", "application/json")
                        .addHeader("X-Requested-With", "XMLHttpRequest")
                        .addHeader("Connection", "close")


                    request = builder.build()
                    chain.proceed(request)
                }
            return@lazy builder.build()
        }




    }



}