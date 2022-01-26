package io.flaterlab.meshexam.data.datasource

import io.flaterlab.meshexam.domain.api.datasource.AdvertisingDataSource
import io.flaterlab.meshexam.library.nearby.api.NearbyFacade
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject

internal class AdvertisingDataSourceImpl @Inject constructor(

) : AdvertisingDataSource {

    override fun startExam(examId: String): Flow<Unit> {
        // TODO: add actual info
        return emptyFlow()
    }
}