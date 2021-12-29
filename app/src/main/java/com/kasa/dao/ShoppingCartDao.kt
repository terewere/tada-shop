package com.kasa.dao

import androidx.room.*
import com.kasa.models.CartItem
import com.kasa.models.CartItemWithProduct
import kotlinx.coroutines.flow.Flow

/**
 * All actions to access and modify our [ShoppingCart] database.
 */

@Dao
interface ShoppingCartDao {



    @Query("SELECT * FROM CartItem")
    fun allItems(): Flow<List<CartItemWithProduct>>


    @Query("SELECT SUM(quantity) FROM CartItem")
    fun getItemsCount(): Flow<Int>


    @Query("SELECT * FROM CartItem WHERE productId = :id ")
    suspend fun findById(id: Long): CartItem?


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(itemWithQuantity: CartItem)

    @Query("DELETE from CartItem WHERE productId = :id")
    suspend fun remove(id: Long)

    @Query("DELETE from CartItem")
    suspend fun empty()
}