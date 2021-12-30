package com.kasa.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RemoteKeys(@PrimaryKey val productId: Long, val prevKey: Int?, val nextKey: Int?)