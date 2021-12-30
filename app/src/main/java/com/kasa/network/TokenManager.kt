package com.kasa.network

import android.Manifest.permission_group
import android.content.Context
import android.content.SharedPreferences
import com.kasa.models.User
import com.kasa.utils.Constants.ACCESS_TOKEN
import com.kasa.utils.Constants.USER_EMAIL
import com.kasa.utils.Constants.USER_FIRST_NAME
import com.kasa.utils.Constants.USER_ID
import com.kasa.utils.Constants.USER_IMG_URL
import com.kasa.utils.Constants.USER_LAST_NAME
import com.kasa.utils.Constants.USER_NAME
import com.kasa.utils.Constants.USER_NUMBER
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class TokenManager @Inject constructor(context: Context) {

    val preferences: SharedPreferences by lazy {
        context.getSharedPreferences(
            permission_group.STORAGE,
            Context.MODE_PRIVATE
        )
    }

    private val editor by lazy { preferences.edit() }


    var accessToken: String?
        get() {
            return preferences.getString(ACCESS_TOKEN, null)
        }
        set(accessToken) {
            editor.putString(ACCESS_TOKEN, accessToken).apply()

        }




    var user: User
        get() {
            val user = User()
            user.userId = preferences.getString(USER_ID, null).toString()
            user.number = preferences.getString(USER_NUMBER, null)
            user.name = preferences.getString(USER_NAME, null)
            user.firstName = preferences.getString(USER_FIRST_NAME, null)
            user.lastName = preferences.getString(USER_LAST_NAME, null)
            user.avatar = preferences.getString(USER_IMG_URL, null)
            user.email = preferences.getString(USER_EMAIL, null)
            return user
        }
        set(user) {

            editor.apply {
                putString(USER_ID, user.userId).apply()
                putString(USER_NAME, user.name).apply()
                putString(USER_FIRST_NAME, user.firstName).apply()
                putString(USER_LAST_NAME, user.lastName).apply()
                putString(USER_NUMBER, user.number).apply()
                putString(USER_IMG_URL, user.avatar).apply()
                putString(USER_EMAIL, user.email).apply()
            }

        }

    fun deleteUser() {
        editor.apply {
            remove(USER_ID).apply()
            remove(USER_NAME).apply()
            remove(USER_NUMBER).apply()
            remove(USER_IMG_URL).apply()
            remove(USER_EMAIL).apply()
        }
    }



}