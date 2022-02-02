package io.flaterlab.meshexam.test

import android.app.Application
import timber.log.Timber

class TestApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}