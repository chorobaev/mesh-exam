package io.flaterlab.meshexam.data.worker

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

internal class WorkerScheduler @Inject constructor(
    @ApplicationContext context: Context,
) {

    private val workManager = WorkManager.getInstance(context)

    fun scheduleAttemptFinish(attemptId: String, finishAfterMinutes: Int) {
        val request = OneTimeWorkRequestBuilder<FinishAttemptWorker>()
            .setInputData(workDataOf(FinishAttemptWorker.ATTEMPT_ID_EXTRA to attemptId))
            .setInitialDelay(finishAfterMinutes.toLong(), TimeUnit.MINUTES)
            .addTag(ATTEMPT_FINISHER_TAG)
            .build()
        workManager.enqueue(request)
    }

    fun cancelAllAttemptFinishers() {
        workManager.cancelAllWorkByTag(ATTEMPT_FINISHER_TAG)
    }

    fun scheduleHostingFinish(hostingId: String, finishAfterMinutes: Int) {
        val request = OneTimeWorkRequestBuilder<FinishHostingWorker>()
            .setInputData(workDataOf(FinishHostingWorker.HOSTING_ID_EXTRA to hostingId))
            .setInitialDelay(finishAfterMinutes.toLong(), TimeUnit.MINUTES)
            .addTag(HOSTING_FINISHER_TAG)
            .build()
        workManager.enqueue(request)
    }

    fun cancelAllHostingFinishers() {
        workManager.cancelAllWorkByTag(HOSTING_FINISHER_TAG)
    }

    companion object {
        private const val ATTEMPT_FINISHER_TAG = "ATTEMPT_FINISHER_TAG"
        private const val HOSTING_FINISHER_TAG = "HOSTING_FINISHER_TAG"
    }
}