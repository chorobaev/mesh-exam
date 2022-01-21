package io.flaterlab.meshexam

import android.app.Application
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class MeshExamApp : Application() {

    val isDebug: Boolean get() = BuildConfig.DEBUG

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        initCrashlytics()
    }

    private fun initCrashlytics() {
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(!isDebug)
    }
}