package com.example.chapter_9_proj

import android.app.Application

class CriminalIntentApplication : Application() {
    // MAKING SURE OUR CRIME REPOSITORY IS INITIALIZED AS SOON AS APPLICATION IS READY
    override fun onCreate() {
        super.onCreate()
        CrimeRepository.initialize(this)
    }
}