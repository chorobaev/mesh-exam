package io.flaterlab.meshexam.androidbase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

typealias ViewBindingProvider<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class ViewBindingFragment<T : ViewBinding> : BaseFragment() {

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