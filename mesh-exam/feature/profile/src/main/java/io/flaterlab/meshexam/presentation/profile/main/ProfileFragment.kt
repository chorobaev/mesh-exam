package io.flaterlab.meshexam.presentation.profile.main

import android.os.Bundle
import android.view.View
import dagger.hilt.android.AndroidEntryPoint
import io.flaterlab.meshexam.androidbase.ViewBindingFragment
import io.flaterlab.meshexam.androidbase.ViewBindingProvider
import io.flaterlab.meshexam.androidbase.ext.clickWithDebounce
import io.flaterlab.meshexam.presentation.profile.adapter.HistoryListAdapter
import io.flaterlab.meshexam.presentation.profile.databinding.FragmentProfileBinding
import io.flaterlab.meshexam.presentation.profile.edit.EditProfileDialogFragment
import javax.inject.Inject

@AndroidEntryPoint
internal class ProfileFragment : ViewBindingFragment<FragmentProfileBinding>() {

    private val viewModel: ProfileViewModel by vm()

    @Inject
    lateinit var historyAdapter: HistoryListAdapter

    override val viewBinder: ViewBindingProvider<FragmentProfileBinding>
        get() = FragmentProfileBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()

        viewModel.userProfile.observe(viewLifecycleOwner) { dvo ->
            with(binding) {
                tvTextAvatar.text = dvo.initials
                tvProfileName.text = dvo.fullName
                tvProfileInfo.text = dvo.info
            }
        }
        viewModel.historyItems.observe(viewLifecycleOwner, historyAdapter::submitList)
        viewModel.historyListState.observe(
            viewLifecycleOwner,
            binding.recyclerViewHistory::setState
        )
        viewModel.commandEditName.observe(viewLifecycleOwner) {
            EditProfileDialogFragment.show(childFragmentManager)
        }

        binding.btnEditProfile.clickWithDebounce(action = viewModel::onEditProfileClicked)
    }

    private fun initRecyclerView() = with(binding.recyclerViewHistory) {
        adapter = historyAdapter
        historyAdapter.onItemClickListener = viewModel::onHistoryItemClicked
    }
}