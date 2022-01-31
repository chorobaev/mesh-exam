package io.flaterlab.meshexam.presentation.di

import androidx.fragment.app.Fragment
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.components.FragmentComponent
import io.flaterlab.meshexam.androidbase.GlobalNavControllerProvider

@EntryPoint
@InstallIn(FragmentComponent::class)
internal interface FragmentEntryPoint {

    val globalNavControllerProvider: GlobalNavControllerProvider

    companion object {
        fun resolve(fragment: Fragment) =
            EntryPointAccessors.fromFragment(fragment, FragmentEntryPoint::class.java)
    }
}