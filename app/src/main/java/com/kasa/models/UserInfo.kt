package com.kasa.models


import com.google.gson.annotations.SerializedName
import com.kasa.entities.User


class UserInfo {
    @SerializedName("user")
    lateinit var user: User

    @SerializedName("access_token")
    lateinit var accessToken: String

}