package com.kasa.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.kasa.models.*
import com.kasa.models.RemoteKeys

@Dao
interface ProductDao {

    @Transaction
    @Query("SELECT * FROM Product where categoryId = :categoryId")
    fun getProducts(categoryId: Int): PagingSource<Int, ProductWithImages>

    @Transaction
    @Query("SELECT * FROM Product")
    fun getAllProducts(): PagingSource<Int, ProductWithImages>


    @Transaction
    suspend fun insertProducts(list:List<ProductWithImages>, keys: List<RemoteKeys>) {

//        insertAllKeys(keys);
        list.forEach {
            insertProduct(it.product)
            insertProductImages(it.productImages)

        }
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertProductImages(list: List<ProductImage>)


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertProduct(product: Product)


    @Query("DELETE from Product")
    fun emptyProducts()

    @Query("DELETE from ProductImage")
    fun emptyProductImages()


    @Query("DELETE FROM remotekeys")
    suspend fun clearRemoteKeys()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllKeys(remoteKey: List<RemoteKeys>)


    @Transaction
    suspend fun clear() {

        emptyProducts();
        emptyProductImages();
        clearRemoteKeys();


    }

    @Query("DELETE from Product where categoryId = :categoryId")
    fun clearByCatId(categoryId: Int?)
}



