package io.flaterlab.meshexam.androidbase

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.MainThread
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.navGraphViewModels
import com.google.android.material.snackbar.Snackbar
import io.flaterlab.meshexam.androidbase.BaseFragment.Companion.LAUNCHER
import io.flaterlab.meshexam.androidbase.text.resolve

abstract class BaseFragment : Fragment() {

    protected val viewModels = ArrayList<Lazy<BaseViewModel>>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModels.forEach { provider ->
            provider.value.message.observe(viewLifecycleOwner) { errorText ->
                val message = errorText.resolve(requireContext()) ?: return@observe
                Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    @MainThread
    protected inline fun <reified VM : BaseViewModel> vm(): Lazy<VM> =
        viewModels<VM>().also(viewModels::add)

    @MainThread
    protected inline fun <reified VM : BaseViewModel> activityVm(): Lazy<VM> =
        activityViewModels<VM>().also(viewModels::add)

    @MainThread
    protected inline fun <reified VM : BaseViewModel> navVm(
        @IdRes graphId: Int,
    ): Lazy<VM> = navGraphViewModels<VM>(graphId).also(viewModels::add)

    companion object {
        const val LAUNCHER = "LAUNCHER"
        const val DEPRECATION_MESSAGE = "Use constructor with launcher"
    }
}

fun Parcelable.toBundleArgs() = bundleOf(
    LAUNCHER to this
)

inline fun <reified T : Parcelable> BaseFragment.getLauncher(): T =
    requireArguments().getParcelable<T>(LAUNCHER) as T