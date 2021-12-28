package com.kasa.models

import com.google.gson.annotations.SerializedName

class AccessToken (
    @SerializedName("token_type")
    var tokenType: String? = null,
    @SerializedName("expires_in")
    var expiresIn: Int = 0,
    @SerializedName("access_token")
    var accessToken: String? = null,
    @SerializedName( "refresh_token")
    var refreshToken: String? = null
    )
