package com.kasa.network

import com.kasa.entities.*
import com.kasa.models.Category
import com.kasa.models.ProductWithImages
import com.kasa.models.UserInfo
import retrofit2.Call
import retrofit2.http.*


interface ApiService {

    @POST("verify-phone")
    @FormUrlEncoded
    fun verifyPhone(@Field("number") number: String): Call<String?>

    @POST("verify-token")
    @FormUrlEncoded
    fun verifyToken(
        @Field("number") number: String, @Field(
            "token"
        ) token: String
    ): Call<UserInfo?>


    @POST("users/update")
    @FormUrlEncoded
    fun updateUser(
        @Field("first_name") firstName: String,
        @Field("last_name") lastName: String,
    ): Call<User>


    @GET("products")
    suspend fun getProducts(
        @Query("length") length: Int,
        @Query("category_id") categoryId: Int,
        @Query("page") page: Int
    ): List<ProductWithImages>



    @GET("categories")
     fun getCategories(): Call<List<Category>?>



}
