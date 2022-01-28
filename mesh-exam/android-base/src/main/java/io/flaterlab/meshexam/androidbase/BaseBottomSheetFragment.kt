package io.flaterlab.meshexam.androidbase

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.flaterlab.meshexam.androidbase.BaseBottomSheetDialogFragment.Companion.LAUNCHER

abstract class BaseBottomSheetDialogFragment : BottomSheetDialogFragment() {

    open val isFullScreen: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (isFullScreen) {
            val container = view.parent as ViewGroup
            container.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            applyBottomSheetBehavior {
                skipCollapsed = true
                state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
    }

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