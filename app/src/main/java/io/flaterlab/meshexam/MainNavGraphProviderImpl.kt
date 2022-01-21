package io.flaterlab.meshexam

import io.flaterlab.meshexam.feature.main.MainNavGraphProvider
import javax.inject.Inject

internal class MainNavGraphProviderImpl @Inject constructor() : MainNavGraphProvider {

    override val navGraphId: Int get() = R.navigation.nav_global
}