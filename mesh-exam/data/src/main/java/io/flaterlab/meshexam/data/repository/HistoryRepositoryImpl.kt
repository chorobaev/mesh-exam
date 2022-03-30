package io.flaterlab.meshexam.data.repository

import androidx.room.withTransaction
import io.flaterlab.meshexam.data.database.MeshDatabase
import io.flaterlab.meshexam.data.database.entity.AttemptEntity
import io.flaterlab.meshexam.data.database.entity.host.HostingEntity
import io.flaterlab.meshexam.data.datastore.dao.UserProfileDao
import io.flaterlab.meshexam.domain.profile.model.ExamHistoryModel
import io.flaterlab.meshexam.domain.profile.model.HostingResultItemModel
import io.flaterlab.meshexam.domain.profile.model.HostingResultMetaModel
import io.flaterlab.meshexam.domain.repository.HistoryRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.math.roundToInt

internal class HistoryRepositoryImpl @Inject constructor(
    private val database: MeshDatabase,
    private val userProfileDao: UserProfileDao,
) : HistoryRepository {

    private val examDao = database.examDao()
    private val attemptDao = database.attemptDao()
    private val hostingDao = database.hostingDao()
    private val userDao = database.userDao()

    override fun userExamHistory(): Flow<List<ExamHistoryModel>> {
        return flow {
            val user = userProfileDao.userProfile().first()
            emit(createHistoryModelList(user.id))
        }
    }

    private suspend fun createHistoryModelList(userId: String): List<ExamHistoryModel> {
        return database.withTransaction {
            val attempts = attemptDao.getAttemptsByUserId(userId)
            val hostings = hostingDao.getAll()

            val resultModel = ArrayList<ExamHistoryModel>()
            var attemptIndex = 0
            var hostingIndex = 0
            while (attemptIndex < attempts.size && hostingIndex < hostings.size) {
                val attempt = attempts[attemptIndex]
                val hosting = hostings[hostingIndex]

                val model = if (attempt.createdAt >= hosting.startedAt) {
                    attemptIndex++
                    createAttemptModel(attempt)
                } else {
                    hostingIndex++
                    createHostingModel(hosting)
                }
                resultModel.add(model)
            }

            while (attemptIndex < attempts.size) {
                val model = createAttemptModel(attempts[attemptIndex])
                resultModel.add(model)
                attemptIndex++
            }

            while (hostingIndex < hostings.size) {
                val model = createHostingModel(hostings[hostingIndex])
                resultModel.add(model)
                hostingIndex++
            }

            resultModel
        }
    }

    private suspend fun createAttemptModel(attempt: AttemptEntity): ExamHistoryModel {
        val exam = examDao.getExamById(attempt.examId)
        return ExamHistoryModel(
            id = attempt.attemptId,
            name = exam.name,
            durationInMin = exam.durationInMin,
            isHosting = false,
        )
    }

    private suspend fun createHostingModel(hosting: HostingEntity): ExamHistoryModel {
        val exam = examDao.getExamById(hosting.examId)
        return ExamHistoryModel(
            id = hosting.hostingId,
            name = exam.name,
            durationInMin = exam.durationInMin,
            isHosting = true,
        )
    }

    override fun hostingResultMeta(hostingId: String): Flow<HostingResultMetaModel> {
        return flow {
            val result = database.withTransaction {
                val hosting = hostingDao.getHostingById(hostingId)
                val exam = examDao.getExamById(hosting.examId)
                val attempts = attemptDao.attemptsByHostingId(hostingId).first()
                HostingResultMetaModel(
                    hostingId = hostingId,
                    examName = exam.name,
                    examInfo = exam.type,
                    durationInMillis = hosting.durationInMillis,
                    submissionCount = attempts.count { it.isFinished },
                    expectedSubmissionCount = attempts.size,
                )
            }
            emit(result)
        }
    }

    override fun hostingResults(hostingId: String): Flow<List<HostingResultItemModel>> {
        return attemptDao
            .attemptsByHostingId(hostingId)
            .map { attempts ->
                coroutineScope {
                    attempts.map { attempt ->
                        async {
                            val user = userDao.getUserById(attempt.userId)
                            val totalScore = examDao.getTotalScoreByExamId(attempt.examId)
                                .roundToInt()
                            HostingResultItemModel(
                                id = attempt.attemptId,
                                studentFullName = user.fullName,
                                studentInfo = user.info,
                                status = if (attempt.submittedAt == null) {
                                    HostingResultItemModel.Status.NOT_SUBMITTED
                                } else {
                                    HostingResultItemModel.Status.SUBMITTED
                                },
                                grade = attempt.score?.coerceAtMost(totalScore.toFloat()) ?: 0F,
                                totalGrade = totalScore,
                            )
                        }
                    }.awaitAll()
                }
            }
    }
}