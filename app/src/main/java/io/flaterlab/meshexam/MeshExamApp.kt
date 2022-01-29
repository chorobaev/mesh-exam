package io.flaterlab.meshexam

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class MeshExamApp : Application(), Configuration.Provider {

    val isDebug: Boolean get() = BuildConfig.DEBUG

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        initCrashlytics()
    }

    private fun initCrashlytics() {
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(!isDebug)
    }

    override fun getWorkManagerConfiguration(): Configuration =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}