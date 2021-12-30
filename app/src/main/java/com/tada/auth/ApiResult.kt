package com.tada.auth

import com.tada.models.ApiError

/**
 * Authentication result : success (user details) or error message.
 */
data class ApiResult<T>(
    val success: T? = null,
    val error: ApiError? = null
)
