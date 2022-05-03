package io.flaterlab.meshexam.data.repository

import io.flaterlab.meshexam.core.Mapper
import io.flaterlab.meshexam.data.datastore.dao.AppInfoDao
import io.flaterlab.meshexam.data.datastore.dao.UserProfileDao
import io.flaterlab.meshexam.data.datastore.entity.UserProfileEntity
import io.flaterlab.meshexam.domain.profile.model.AppInfoModel
import io.flaterlab.meshexam.domain.profile.model.UpdateUserProfileModel
import io.flaterlab.meshexam.domain.profile.model.UserProfileModel
import io.flaterlab.meshexam.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class ProfileRepositoryImpl @Inject constructor(
    private val profileDao: UserProfileDao,
    private val appInfoDao: AppInfoDao,
    private val userProfileModelMapper: Mapper<UserProfileEntity, UserProfileModel>,
) : ProfileRepository {

    override fun userProfile(): Flow<UserProfileModel> {
        return profileDao.userProfile()
            .map(userProfileModelMapper::invoke)
    }

    override suspend fun updateUserProfile(profile: UpdateUserProfileModel) {
        profileDao.updateUserProfile(
            firstName = profile.firstName,
            lastName = profile.lastName,
            info = profile.info,
        )
    }

    override suspend fun getAppInfo(): AppInfoModel {
        val info = appInfoDao.getAppInfo()
        appInfoDao.updateAppInfo(info.copy(isFirstStartUp = false))
        return AppInfoModel(
            isInitialStartUp = info.isFirstStartUp
        )
    }
}