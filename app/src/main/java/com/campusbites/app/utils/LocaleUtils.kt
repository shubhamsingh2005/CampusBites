package com.campusbites.app.utils

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.LocaleList
import java.util.*

object LocaleUtils {

    private const val LANGUAGE_PREF_FILE = "language_pref"
    private const val LANGUAGE_KEY = "lang_code"

    fun getSavedLanguage(context: Context): String {
        val prefs = context.getSharedPreferences(LANGUAGE_PREF_FILE, Context.MODE_PRIVATE)
        return prefs.getString(LANGUAGE_KEY, "en") ?: "en"
    }

    fun setLocale(context: Context, language: String): Context {
        saveLanguage(context, language)

        val locale = Locale(language)
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)

        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> {
                config.setLocale(locale)
                config.setLocales(LocaleList(locale))
                return context.createConfigurationContext(config)
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 -> {
                config.setLocale(locale)
                return context.createConfigurationContext(config)
            }
            else -> {
                config.locale = locale
                context.resources.updateConfiguration(config, context.resources.displayMetrics)
                return context
            }
        }
    }

    private fun saveLanguage(context: Context, language: String) {
        val prefs = context.getSharedPreferences(LANGUAGE_PREF_FILE, Context.MODE_PRIVATE)
        prefs.edit().putString(LANGUAGE_KEY, language).apply()
    }
}
