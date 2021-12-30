package com.kasa.databases

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.kasa.dao.CategoryDao
import com.kasa.dao.ProductDao
import com.kasa.dao.ShoppingCartDao
import com.kasa.dao.RemoteKeysDao
import com.kasa.models.*
import java.util.*


@Database(
    entities = [
        User::class,
        Product::class,
        ProductImage::class,
        Category::class,
        CartItem::class,
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
    abstract fun getCartDao(): ShoppingCartDao
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



