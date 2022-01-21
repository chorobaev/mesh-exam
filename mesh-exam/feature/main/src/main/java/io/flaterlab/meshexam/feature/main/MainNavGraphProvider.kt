package io.flaterlab.meshexam.feature.main

import androidx.annotation.NavigationRes

interface MainNavGraphProvider {

    @get:NavigationRes val navGraphId: Int
}