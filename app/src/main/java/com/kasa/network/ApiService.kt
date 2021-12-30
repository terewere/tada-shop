package com.kasa.network

import com.kasa.models.Category
import com.kasa.models.Order
import com.kasa.models.ProductWithImages
import com.kasa.models.UserInfo
import retrofit2.Call
import retrofit2.http.*


interface ApiService {


    @POST("login")
    @FormUrlEncoded
    suspend fun login(
        @Field("phone") number: String): UserInfo


    @POST("register")
    @FormUrlEncoded
   suspend fun register(
        @Field("first_name") firstName: String,
        @Field("last_name") lastName: String,
        @Field("email") email: String,
        @Field("phone") phone: String,
    ): UserInfo


    @GET("products")
    suspend fun getProducts(
        @Query("length") length: Int,
        @Query("category_id") categoryId: Int,
        @Query("page") page: Int
    ): List<ProductWithImages>



    @GET("categories")
     fun getCategories(): Call<List<Category>?>


    @GET("orders")
    suspend fun getOrders(): List<Order>?


    @GET("home-slides")
    suspend fun getHomeSlides(): List<String>?

}
