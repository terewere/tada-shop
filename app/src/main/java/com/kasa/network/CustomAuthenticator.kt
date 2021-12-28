package com.kasa.network

import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CustomAuthenticator
@Inject
constructor( val tokenManager: TokenManager) :  Authenticator {


    @Throws(IOException::class)
    override fun authenticate(route: Route?, response: Response): Request? {
        if (responseCount(response) >= 3) {
            return null
        }

          return  response.request.newBuilder()
                .header("Authorization", "Bearer " + tokenManager.accessToken).build()

    }

    private fun responseCount(res: Response): Int {
        var response = res
        var result = 1
        while (response.priorResponse.also { response = it!! } != null) {
            result++
        }
        return result
    }


}