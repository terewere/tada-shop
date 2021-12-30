package com.tada.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class CartItem(
    @PrimaryKey()
    val productId: Long,
    val quantity: Long):Parcelable

