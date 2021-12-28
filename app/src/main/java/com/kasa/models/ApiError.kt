package com.kasa.models


import com.google.gson.annotations.SerializedName


class ApiError {
    @SerializedName("message")
    var message: String? = null

    @SerializedName("errors")
    var errors: Map<String, List<String>>? = null

}