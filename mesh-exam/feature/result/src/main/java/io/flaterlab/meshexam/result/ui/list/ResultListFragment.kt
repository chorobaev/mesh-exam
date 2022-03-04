package io.flaterlab.meshexam.result.ui.list

import android.os.Bundle
import android.view.View
import dagger.hilt.android.AndroidEntryPoint
import io.flaterlab.meshexam.androidbase.ViewBindingFragment
import io.flaterlab.meshexam.androidbase.ViewBindingProvider
import io.flaterlab.meshexam.result.databinding.FragmentResultListBinding

@AndroidEntryPoint
internal class ResultListFragment : ViewBindingFragment<FragmentResultListBinding>() {

    override val viewBinder: ViewBindingProvider<FragmentResultListBinding>
        get() = FragmentResultListBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
}