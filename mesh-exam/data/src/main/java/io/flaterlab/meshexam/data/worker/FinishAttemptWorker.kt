package io.flaterlab.meshexam.data.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.hilt.android.EntryPointAccessors
import io.flaterlab.meshexam.data.di.WorkerEntryPoint
import timber.log.Timber

internal class FinishAttemptWorker(
    context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params) {

    private val entryPoint = EntryPointAccessors
        .fromApplication(context.applicationContext, WorkerEntryPoint::class.java)

    override suspend fun doWork(): Result {
        val attemptId: String = inputData.getString(ATTEMPT_ID_EXTRA) ?: return Result.success()
        try {
            entryPoint.attemptRepository.finishAttemptBySystem(attemptId)
        } catch (ex: Exception) {
            Timber.e(ex)
            return Result.failure()
        }
        return Result.success()
    }

    companion object {
        const val ATTEMPT_ID_EXTRA = "ATTEMPT_ID_EXTRA"
    }
}