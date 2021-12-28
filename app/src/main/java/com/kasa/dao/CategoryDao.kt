package com.kasa.dao
import androidx.lifecycle.LiveData
import androidx.room.*
import com.kasa.models.*

@Dao
interface CategoryDao {

    @Query("SELECT * FROM Category")
     fun getCategories(): LiveData<List<Category>?>


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(category: Category?)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertCategories(list: List<Category>?)


    @Query("DELETE from Category")
    fun empty()

}



