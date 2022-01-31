package io.flaterlab.meshexam.feature.meshroom

import android.os.Bundle
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.core.text.toSpannable
import dagger.hilt.android.AndroidEntryPoint
import io.flaterlab.meshexam.androidbase.TextWatcherManager
import io.flaterlab.meshexam.androidbase.ViewBindingFragment
import io.flaterlab.meshexam.androidbase.ViewBindingProvider
import io.flaterlab.meshexam.androidbase.bindTextWatcher
import io.flaterlab.meshexam.androidbase.common.adapter.ClientListAdapter
import io.flaterlab.meshexam.feature.meshroom.databinding.FragmentMeshRoomBinding
import io.flaterlab.meshexam.feature.meshroom.dvo.ClientDvo
import io.flaterlab.meshexam.uikit.ext.getColorAttr
import javax.inject.Inject

@AndroidEntryPoint
internal class MeshRoomFragment : ViewBindingFragment<FragmentMeshRoomBinding>() {

    private val viewModel: MeshRoomViewModel by vm()
    private val watcherManager = TextWatcherManager(this)

    @Inject
    lateinit var clientListAdapter: ClientListAdapter

    override val viewBinder: ViewBindingProvider<FragmentMeshRoomBinding>
        get() = FragmentMeshRoomBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initSearch()

        viewModel.studentAmount.observe(viewLifecycleOwner, ::setStudentAmount)
        viewModel.exam.observe(viewLifecycleOwner) { exam ->
            setExamName(exam.name)
        }
        viewModel.clients.observe(viewLifecycleOwner, clientListAdapter::submitList)
    }

    private fun initRecyclerView() = with(binding.recyclerViewMeshClients) {
        adapter = clientListAdapter
        clientListAdapter.onClientClickListener = { viewModel.onClientClicked(it as ClientDvo) }
    }

    private fun initSearch() = with(binding.etMeshSearch) {
        bindTextWatcher(watcherManager) { text ->
            viewModel.onSearchTextChanged(text?.toString())
        }
    }

    private fun setStudentAmount(amount: Int) {
        val title = getString(R.string.mesh_students_amount_title, amount)
        val startIndex = title.lastIndexOf(amount.toString())
        val spanned = title.toSpannable().apply {
            setSpan(
                ForegroundColorSpan(requireContext().getColorAttr(R.attr.colorOnBackground)),
                startIndex,
                startIndex + amount.toString().length,
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE
            )
        }
        binding.tvMeshStudentAmount.text = spanned
    }

    private fun setExamName(name: String) {
        val title = getString(R.string.mesh_exam_name_title, name)
        val startIndex = title.lastIndexOf(name)
        val spanned = title.toSpannable().apply {
            setSpan(
                ForegroundColorSpan(requireContext().getColorAttr(R.attr.colorOnBackground)),
                startIndex,
                startIndex + name.length,
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE
            )
        }
        binding.tvMeshExamName.text = spanned
    }
}