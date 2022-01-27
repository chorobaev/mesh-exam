package io.flaterlab.meshexam.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import io.flaterlab.meshexam.androidbase.BaseFragment
import io.flaterlab.meshexam.create.databinding.FragmentCreateExamBinding

@AndroidEntryPoint
class CreateExamFragment : BaseFragment() {

    private var _binding: FragmentCreateExamBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateExamBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}