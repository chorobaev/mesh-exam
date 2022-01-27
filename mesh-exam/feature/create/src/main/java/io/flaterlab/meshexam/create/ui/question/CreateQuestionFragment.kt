package io.flaterlab.meshexam.create.ui.question

import android.os.Bundle
import android.view.View
import dagger.hilt.android.AndroidEntryPoint
import io.flaterlab.meshexam.androidbase.ViewBindingFragment
import io.flaterlab.meshexam.androidbase.ViewBindingProvider
import io.flaterlab.meshexam.create.databinding.FragmentCreateQuestionBinding

@AndroidEntryPoint
internal class CreateQuestionFragment : ViewBindingFragment<FragmentCreateQuestionBinding>() {

    override val viewBinder: ViewBindingProvider<FragmentCreateQuestionBinding>
        get() = FragmentCreateQuestionBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}