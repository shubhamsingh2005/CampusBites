package com.campusbites.app.utils

import android.content.Context
import android.content.SharedPreferences

object TokenManager {
    private const val PREFS_NAME = "jwt_prefs"
    private const val TOKEN_KEY = "jwt_token"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun saveToken(context: Context, token: String) {
        getPrefs(context).edit().putString(TOKEN_KEY, token).apply()
    }

    fun getToken(context: Context): String? {
        return getPrefs(context).getString(TOKEN_KEY, null)
    }

    fun clearToken(context: Context) {
        getPrefs(context).edit().remove(TOKEN_KEY).apply()
    }
}
