package io.flaterlab.meshexam.presentation.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import io.flaterlab.meshexam.androidbase.GlobalNavControllerProvider
import io.flaterlab.meshexam.presentation.exams.router.ExamsRouter
import io.flaterlab.meshexam.presentation.router.ExamsRouterImpl

@Module
@InstallIn(FragmentComponent::class)
internal interface RouterBindingModule {

    @Binds
    fun bindGlobalNavControllerProvider(impl: GlobalNavControllerProviderImpl): GlobalNavControllerProvider

    @Binds
    fun bindExamRouter(impl: ExamsRouterImpl): ExamsRouter
}