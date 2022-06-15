package io.flaterlab.meshexam.presentation.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import io.flaterlab.meshexam.androidbase.GlobalNavControllerProvider
import io.flaterlab.meshexam.create.router.CreateExamRouter
import io.flaterlab.meshexam.create.router.CreateQuestionRouter
import io.flaterlab.meshexam.feature.meshroom.router.MeshroomRouter
import io.flaterlab.meshexam.onboarding.ui.OnboardingRouter
import io.flaterlab.meshexam.presentation.discover.router.DiscoverRouter
import io.flaterlab.meshexam.presentation.exams.router.ExamsRouter
import io.flaterlab.meshexam.presentation.profile.router.ProfileRouter
import io.flaterlab.meshexam.presentation.router.*

@Module
@InstallIn(FragmentComponent::class)
internal interface RouterBindingModule {

    @Binds
    fun bindGlobalNavControllerProvider(impl: GlobalNavControllerProviderImpl): GlobalNavControllerProvider

    @Binds
    fun bindExamRouter(impl: ExamsRouterImpl): ExamsRouter

    @Binds
    fun bindDiscoverRouter(impl: DiscoverRouterImpl): DiscoverRouter

    @Binds
    fun bindMeshroomRouter(impl: MeshroomRouterImpl): MeshroomRouter

    @Binds
    fun bindProfileRouter(impl: ProfileRouterImpl): ProfileRouter

    @Binds
    fun bindCreateExamRouter(impl: CreateExamRouterImpl): CreateExamRouter

    @Binds
    fun bindCreateQuestionRouter(impl: CreateQuestionRouterImpl): CreateQuestionRouter

    @Binds
    fun bindOnboardingRouter(impl: OnboardingRouterImpl): OnboardingRouter
}