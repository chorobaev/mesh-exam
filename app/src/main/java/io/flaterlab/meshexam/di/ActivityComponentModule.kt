package io.flaterlab.meshexam.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import io.flaterlab.meshexam.MainNavGraphProviderImpl
import io.flaterlab.meshexam.feature.main.MainNavGraphProvider


@Module
@InstallIn(ActivityComponent::class)
internal abstract class ActivityComponentModule {

    @Binds
    abstract fun bindMainNavGraphProvider(impl: MainNavGraphProviderImpl): MainNavGraphProvider
}