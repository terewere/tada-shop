package com.kasa.databases

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.kasa.dao.CategoryDao
import com.kasa.dao.ProductDao
import com.kasa.entities.*
import com.kasa.models.Category
import com.kasa.models.Product
import com.kasa.models.ProductImage
import com.kasa.paging3.RemoteKeys
import com.kasa.paging3.RemoteKeysDao
import java.util.*


@Database(
    entities = [
        User::class,
        Product::class,
        ProductImage::class,
        Category::class,
        RemoteKeys::class,
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class DB : RoomDatabase() {

    abstract fun productDao(): ProductDao
    abstract fun categoryDao(): CategoryDao
    abstract fun getRepoDao(): RemoteKeysDao
}

class Converters {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }


    @TypeConverter
    fun stringToList(value: String?): List<String>? {
        return value?.split(",")
    }

    @TypeConverter
    fun listToString(value: List<String>?): String? {
        return value?.joinToString(",")
    }


}



