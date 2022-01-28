package io.flaterlab.meshexam.androidbase

import android.os.Parcelable
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import io.flaterlab.meshexam.androidbase.BaseFragment.Companion.LAUNCHER

abstract class BaseFragment : Fragment() {

    companion object {
        const val LAUNCHER = "LAUNCHER"
        const val DEPRECATION_MESSAGE = "Use constructor with launcher"
    }
}

fun Parcelable.toBundleArgs() = bundleOf(
    LAUNCHER to this
)