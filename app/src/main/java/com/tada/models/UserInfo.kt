package com.tada.models


import com.google.gson.annotations.SerializedName


class UserInfo {
    @SerializedName("user")
    lateinit var user: User

    @SerializedName("access_token")
    lateinit var accessToken: String

}