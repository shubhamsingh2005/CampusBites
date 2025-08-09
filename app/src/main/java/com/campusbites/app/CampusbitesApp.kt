package com.campusbites.app

import android.app.Application
import org.maplibre.android.MapLibre

class CampusbitesApp : Application() {
    override fun onCreate() {
        super.onCreate()
        MapLibre.getInstance(this)
    }
}
