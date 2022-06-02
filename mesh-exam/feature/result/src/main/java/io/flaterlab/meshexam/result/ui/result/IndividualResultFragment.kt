package io.flaterlab.meshexam.result.ui.result

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import io.flaterlab.meshexam.androidbase.ViewBindingFragment
import io.flaterlab.meshexam.androidbase.ViewBindingProvider
import io.flaterlab.meshexam.androidbase.ext.applyLayoutParams
import io.flaterlab.meshexam.androidbase.ext.clickWithDebounce
import io.flaterlab.meshexam.androidbase.text.Text
import io.flaterlab.meshexam.androidbase.text.setText
import io.flaterlab.meshexam.androidbase.toBundleArgs
import io.flaterlab.meshexam.result.R
import io.flaterlab.meshexam.result.databinding.FragmentIndividualResultBinding
import io.flaterlab.meshexam.result.databinding.ItemGeneralInfoBinding
import io.flaterlab.meshexam.result.ui.result.adapter.ResultQuestionPagerAdapter
import io.flaterlab.meshexam.result.ui.send.SendResultLauncher
import javax.inject.Inject
import javax.inject.Provider

@AndroidEntryPoint
internal class IndividualResultFragment : ViewBindingFragment<FragmentIndividualResultBinding>() {

    private val viewModel: IndividualResultViewModel by vm()

    @Inject
    lateinit var pagerAdapterProvider: Provider<ResultQuestionPagerAdapter>
    private val pagerAdapter
        get() = binding.viewPagerQuestions.adapter as ResultQuestionPagerAdapter

    override val viewBinder: ViewBindingProvider<FragmentIndividualResultBinding>
        get() = FragmentIndividualResultBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initGeneralInfoToggle()
        initViewPager()
        initTabStyler()

        viewModel.metaInfo.observe(viewLifecycleOwner) { dvo ->
            with(binding) {
                toolbarExamination.setTitle(dvo.name)
                toolbarExamination.setSubtitle(dvo.info)
                onGeneralInfoReceived(dvo.generalInfoList)
                pagerAdapter.submitList(dvo.questionInfoList)
            }
        }
        viewModel.sendingEnabled.observe(viewLifecycleOwner, binding.ivSendResult::isVisible::set)
        viewModel.commandSendResult.observe(viewLifecycleOwner) { attemptId ->
            findNavController().navigate(
                R.id.action_individualResultFragment_to_sendResultFragment,
                SendResultLauncher(attemptId).toBundleArgs(),
            )
        }

        binding.ivSendResult.clickWithDebounce(action = viewModel::onSendResultClicked)
    }

    private fun onGeneralInfoReceived(infoList: List<Pair<Text, Text>>) =
        with(binding.containerGeneralInfoList) {
            removeAllViews()
            infoList.forEach { info ->
                ItemGeneralInfoBinding.inflate(layoutInflater, this, true)
                    .apply {
                        tvGeneralInfoKey.setText(info.first)
                        tvGeneralInfoValue.setText(info.second)
                    }
            }
        }

    private fun initGeneralInfoToggle() {
        binding.tvGeneralInfoToggle.setOnClickListener {
            binding.containerGeneralInfoList.isVisible = !binding.containerGeneralInfoList.isVisible
            val drawableRes = if (binding.containerGeneralInfoList.isVisible) {
                R.drawable.ic_chevron_up
            } else {
                R.drawable.ic_chevron_down
            }
            binding.tvGeneralInfoToggle
                .setCompoundDrawablesWithIntrinsicBounds(0, 0, drawableRes, 0)
        }
    }

    private fun initViewPager() = with(binding.viewPagerQuestions) {
        adapter = pagerAdapterProvider.get()
        TabLayoutMediator(binding.tabLayoutNumbers, this) { tab, position ->
            tab.text = position.plus(1).toString()
            tab.view.setBackgroundResource(
                if (pagerAdapter.getItemAt(position).isCorrect) {
                    R.drawable.selector_tab_background
                } else {
                    R.drawable.selector_tab_background_incorrect
                }
            )
        }.attach()
    }

    private fun initTabStyler() {
        val side = resources.getDimension(R.dimen.tab_side).toInt()
        val margin = resources.getDimension(R.dimen.margin_medium).toInt()
        val tabContainer = binding.tabLayoutNumbers.children.first() as ViewGroup
        tabContainer.setOnHierarchyChangeListener(object : ViewGroup.OnHierarchyChangeListener {
            override fun onChildViewAdded(parent: View?, child: View?) {
                child?.applyLayoutParams<LinearLayout.LayoutParams> {
                    weight = 0F
                    height = side
                    width = side
                    marginStart = margin
                }
                binding.tabLayoutNumbers.requestLayout()
            }

            override fun onChildViewRemoved(parent: View?, child: View?) = Unit
        })
    }
}