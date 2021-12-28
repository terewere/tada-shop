package com.kasa.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Product(
    @PrimaryKey
    @SerializedName("id")
    val id: Long,
    @SerializedName("label")
    val label: String,
    @SerializedName("categoryId")
    val categoryId: Int,
    @SerializedName("imgUrl")
    val imgUrl: String,
    @SerializedName("active")
    val active: Int = 0,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("price")
    val price: Double? = null,



) : Parcelable

