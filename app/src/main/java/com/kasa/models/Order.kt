package com.kasa.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Order(
    @SerializedName("id")
    val id: String,
    @SerializedName("productName")
    val productName: String,
    @SerializedName("productUrl")
    val productUrl: String,
    @SerializedName("quantity")
    val quantity: Long,
    @SerializedName("total")
    val total: Long,

    @SerializedName("status")
    val status: String,
    ): Parcelable