package io.flaterlab.meshexam.result.ui.receive

import android.os.Bundle
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.core.text.toSpannable
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import io.flaterlab.meshexam.androidbase.ViewBindingFragment
import io.flaterlab.meshexam.androidbase.ViewBindingProvider
import io.flaterlab.meshexam.androidbase.common.adapter.ClientListAdapter
import io.flaterlab.meshexam.result.R
import io.flaterlab.meshexam.result.databinding.FragmentReceiveResultBinding
import io.flaterlab.meshexam.result.dvo.ResultItemDvo
import io.flaterlab.meshexam.uikit.ext.getColorAttr
import javax.inject.Inject

@AndroidEntryPoint
internal class ReceiveResultFragment : ViewBindingFragment<FragmentReceiveResultBinding>() {

    @Inject
    lateinit var resultListAdapter: ClientListAdapter

    private val viewModel: ReceiveResultViewModel by viewModels()

    override val viewBinder: ViewBindingProvider<FragmentReceiveResultBinding>
        get() = FragmentReceiveResultBinding::inflate


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        initObservers()
    }

    private fun initObservers() {
        viewModel.examInfo.observe(viewLifecycleOwner) { meta ->
            with(binding) {
                toolbarHostResult.setTitle(meta.examName)
                toolbarHostResult.setSubtitle(meta.info)
                setDuration(meta.duration)
                setSubmissionAmount(meta.submissionCount to meta.expectedSubmissionCount)
            }
        }
        viewModel.resultListState.observe(viewLifecycleOwner, binding.recyclerViewResults::setState)
        viewModel.resultList.observe(viewLifecycleOwner, resultListAdapter::submitList)
    }

    private fun initRecyclerView() = with(binding.recyclerViewResults) {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = resultListAdapter
        resultListAdapter.onClientClickListener = { dvo ->
            viewModel.onResultClicked(dvo as ResultItemDvo)
        }
    }

    private fun setDuration(time: String) {
        val text = getString(R.string.result_list_time, time)
        val startIndex = text.indexOf(':') + 1
        val spanned = text.toSpannable().apply {
            setSpan(
                ForegroundColorSpan(requireContext().getColorAttr(R.attr.colorOnBackground)),
                startIndex,
                text.length,
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE
            )
        }
        binding.tvHostResultTime.text = spanned
    }

    private fun setSubmissionAmount(amount: Pair<Int, Int>) {
        val title = getString(R.string.result_list_submitted, amount.first, amount.second)
        val startIndex = title.lastIndexOf(":") + 1
        val spanned = title.toSpannable().apply {
            setSpan(
                ForegroundColorSpan(requireContext().getColorAttr(R.attr.colorOnBackground)),
                startIndex,
                title.length,
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE
            )
        }
        binding.tvHostResultSubmitted.text = spanned
    }
}