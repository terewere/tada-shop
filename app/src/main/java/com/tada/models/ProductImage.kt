package com.tada.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class ProductImage(
    @PrimaryKey
    @SerializedName("id")
    val id:Long,
    @SerializedName("imgUrl")
    val imgUrl: String,
    @SerializedName("productId")
    val productId: String
): Parcelable