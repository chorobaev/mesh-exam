package io.flaterlab.meshexam.androidbase

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.MainThread
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.navGraphViewModels
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import io.flaterlab.meshexam.androidbase.BaseBottomSheetDialogFragment.Companion.LAUNCHER
import io.flaterlab.meshexam.androidbase.text.resolve

abstract class BaseBottomSheetDialogFragment : BottomSheetDialogFragment() {

    protected val viewModels = ArrayList<Lazy<BaseViewModel>>()

    open val isFullScreen: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupFullScreen()
        observeViewModels()
    }

    private fun setupFullScreen() {
        if (isFullScreen) {
            val container = requireView().parent as ViewGroup
            container.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            applyBottomSheetBehavior {
                skipCollapsed = true
                state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
    }

    private fun observeViewModels() {
        viewModels.forEach { provider ->
            provider.value.message.observe(viewLifecycleOwner) { errorText ->
                val message = errorText.resolve(requireContext()) ?: return@observe
                Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
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
        const val LAUNCHER = BaseFragment.LAUNCHER
        const val DEPRECATION_MESSAGE = BaseFragment.DEPRECATION_MESSAGE
    }
}

abstract class ViewBindingBottomSheetDialogFragment<T : ViewBinding> :
    BaseBottomSheetDialogFragment() {

    private var _binding: T? = null
    protected val binding: T get() = _binding ?: throw IllegalStateException("ViewBinding is null")

    abstract val viewBinder: ViewBindingProvider<T>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = viewBinder(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

fun BaseBottomSheetDialogFragment.applyBottomSheetBehavior(
    block: BottomSheetBehavior<View>.() -> Unit
) {
    BottomSheetBehavior.from(requireView().parent as View).block()
}

fun BaseBottomSheetDialogFragment.setLauncher(launcher: Parcelable) {
    arguments = bundleOf(
        LAUNCHER to launcher
    )
}