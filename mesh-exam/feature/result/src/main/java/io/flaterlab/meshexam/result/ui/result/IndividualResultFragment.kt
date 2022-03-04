package io.flaterlab.meshexam.result.ui.result

import android.os.Bundle
import android.view.View
import dagger.hilt.android.AndroidEntryPoint
import io.flaterlab.meshexam.androidbase.ViewBindingFragment
import io.flaterlab.meshexam.androidbase.ViewBindingProvider
import io.flaterlab.meshexam.result.databinding.FragmentIndividualResultBinding

@AndroidEntryPoint
internal class IndividualResultFragment : ViewBindingFragment<FragmentIndividualResultBinding>() {

    override val viewBinder: ViewBindingProvider<FragmentIndividualResultBinding>
        get() = FragmentIndividualResultBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
}