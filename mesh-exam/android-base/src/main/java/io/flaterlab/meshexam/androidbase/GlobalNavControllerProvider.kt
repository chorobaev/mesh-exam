package io.flaterlab.meshexam.androidbase

import androidx.navigation.NavController

interface GlobalNavControllerProvider {

    fun get(): NavController
}