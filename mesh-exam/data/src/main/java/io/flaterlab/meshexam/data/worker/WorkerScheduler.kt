package io.flaterlab.meshexam.data.worker

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

internal class WorkerScheduler @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    fun scheduleAttemptFinish(attemptId: String, finishAfterMinutes: Int) {
        val request = OneTimeWorkRequestBuilder<FinishAttemptWorker>()
            .setInputData(workDataOf(FinishAttemptWorker.ATTEMPT_ID_EXTRA to attemptId))
            .setInitialDelay(finishAfterMinutes.toLong(), TimeUnit.MINUTES)
            .build()
        WorkManager.getInstance(context).enqueue(request)
    }

    fun scheduleHostingFinish(hostingId: String, finishAfterMinutes: Int) {
        val request = OneTimeWorkRequestBuilder<FinishHostingWorker>()
            .setInputData(workDataOf(FinishHostingWorker.HOSTING_ID_EXTRA to hostingId))
            .setInitialDelay(finishAfterMinutes.toLong(), TimeUnit.MINUTES)
            .build()
        WorkManager.getInstance(context).enqueue(request)
    }
}