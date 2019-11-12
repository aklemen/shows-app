package com.aklemen.shows

import android.app.Application
import android.content.SharedPreferences
import android.preference.PreferenceManager

class ShowsApplication : Application() {

    companion object{

        lateinit var sharedPreferences : SharedPreferences
    }

    override fun onCreate() {
        super.onCreate()

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
    }

}