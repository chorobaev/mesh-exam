package io.flaterlab.meshexam.feature.meshroom.ui.finishing

import android.os.Bundle
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.activity.addCallback
import androidx.core.text.toSpannable
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import io.flaterlab.meshexam.androidbase.ViewBindingFragment
import io.flaterlab.meshexam.androidbase.ViewBindingProvider
import io.flaterlab.meshexam.androidbase.common.adapter.ClientListAdapter
import io.flaterlab.meshexam.androidbase.ext.clickWithDebounce
import io.flaterlab.meshexam.androidbase.ext.showAlert
import io.flaterlab.meshexam.feature.meshroom.R
import io.flaterlab.meshexam.feature.meshroom.databinding.FragmentFinishingBinding
import io.flaterlab.meshexam.uikit.ext.getColorAttr
import javax.inject.Inject

@AndroidEntryPoint
internal class FinishingFragment : ViewBindingFragment<FragmentFinishingBinding>() {

    private val viewModel: FinishingViewModel by vm()

    @Inject
    lateinit var submissionListAdapter: ClientListAdapter

    override val viewBinder: ViewBindingProvider<FragmentFinishingBinding>
        get() = FragmentFinishingBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            viewModel.onBackPressed()
        }

        viewModel.submissionAmount.observe(viewLifecycleOwner, ::setSubmissionAmount)
        viewModel.submissionListStat.observe(
            viewLifecycleOwner,
            binding.recyclerViewStudents::setState
        )
        viewModel.submissionList.observe(viewLifecycleOwner, submissionListAdapter::submitList)
        viewModel.commandConfirmFinish.observe(viewLifecycleOwner) {
            showAlert(
                message = getString(R.string.monitor_finishImmediatelyConfirmMessage),
                positive = getString(R.string.common_yes),
                negative = getString(R.string.common_no),
                positiveCallback = { viewModel.onFinishImmediatelyConfirmed() }
            )
        }
        viewModel.commandOpenResult.observe(viewLifecycleOwner) { attempId ->
            // TODO: implement navigation
        }

        binding.btnForceFinish.clickWithDebounce(action = viewModel::onFinishImmediatelyClicked)
    }

    private fun initRecyclerView() = with(binding.recyclerViewStudents) {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = submissionListAdapter
    }

    private fun setSubmissionAmount(amount: Pair<Int, Int>) {
        val title = getString(R.string.monitor_finishing_amountTitle, amount.first, amount.second)
        val startIndex = title.lastIndexOf(":") + 1
        val spanned = title.toSpannable().apply {
            setSpan(
                ForegroundColorSpan(requireContext().getColorAttr(R.attr.colorOnBackground)),
                startIndex,
                title.length,
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE
            )
        }
        binding.tvSubmittedAmount.text = spanned
    }
}