package com.kasa.auth

import com.kasa.models.ApiError

/**
 * Authentication result : success (user details) or error message.
 */
data class ApiResult<T>(
    val success: T? = null,
    val error: ApiError? = null
)
