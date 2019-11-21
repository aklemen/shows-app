package com.aklemen.shows.util

import android.app.Application
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.widget.ProgressBar
import com.aklemen.shows.ui.login.LoginActivity

class ShowsApp : Application() {

    companion object {

        lateinit var instance: ShowsApp

        lateinit var sharedPreferences: SharedPreferences

        fun getToken() : String = sharedPreferences.getString(LoginActivity.PREF_TOKEN, "") ?: ""
        fun setToken(token: String) = sharedPreferences.edit().putString(LoginActivity.PREF_TOKEN, token).apply()

        fun getRememberMe(): Boolean = sharedPreferences.getBoolean(LoginActivity.PREF_REMEMBER_ME, false)
        fun setRememberMe(rememberMe: Boolean) = sharedPreferences.edit().putBoolean(LoginActivity.PREF_REMEMBER_ME, rememberMe).apply()

        fun clearPrefs() = sharedPreferences.edit().clear().commit()
    }

    override fun onCreate() {
        super.onCreate()

        instance = this
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

    }
}