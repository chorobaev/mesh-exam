package io.flaterlab.meshexam.examination.ui.exam

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.addCallback
import androidx.core.view.children
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import io.flaterlab.meshexam.androidbase.ViewBindingFragment
import io.flaterlab.meshexam.androidbase.ViewBindingProvider
import io.flaterlab.meshexam.androidbase.ext.applyLayoutParams
import io.flaterlab.meshexam.androidbase.ext.clickWithDebounce
import io.flaterlab.meshexam.androidbase.ext.showAlert
import io.flaterlab.meshexam.androidbase.toBundleArgs
import io.flaterlab.meshexam.examination.R
import io.flaterlab.meshexam.examination.databinding.FragmentExamBinding
import io.flaterlab.meshexam.examination.ui.exam.adapter.QuestionPagerAdapter
import io.flaterlab.meshexam.examination.ui.result.ResultLauncher
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@AndroidEntryPoint
internal class ExamFragment : ViewBindingFragment<FragmentExamBinding>() {

    private val pagerAdapter get() = binding.viewPagerQuestions.adapter as QuestionPagerAdapter

    private val viewModel: ExamViewModel by vm()

    private var questionSelectionJobs: MutableList<Job> = ArrayList()

    override val viewBinder: ViewBindingProvider<FragmentExamBinding>
        get() = FragmentExamBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBackPressHandle()
        initViewPager()
        initTabStyler()
        observeViewModel()
        initClickListeners()
    }

    private fun initBackPressHandle() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            viewModel.onBackPressed()
        }
    }

    private fun observeViewModel() {
        viewModel.examMeta.observe(viewLifecycleOwner) { meta ->
            binding.toolbarExamination.run {
                setTitle(meta.name)
                if (meta.info.isNotBlank()) setSubtitle(meta.info)
            }
        }
        viewModel.timeLeft.observe(viewLifecycleOwner, binding.tvExamTimer::setText)
        viewModel.questionIds.observe(viewLifecycleOwner) { list ->
            questionSelectionJobs.forEach { it.cancel() }
            questionSelectionJobs.clear()
            pagerAdapter.submitList(list)
        }

        viewModel.commandShowFinishingConfirm.observe(viewLifecycleOwner) {
            showAlert(
                message = getString(R.string.exam_main_leaveConfirmMessage),
                positive = getString(R.string.common_yes),
                negative = getString(R.string.common_no),
                positiveCallback = { viewModel.onFinishConfirmed() }
            )
        }
        viewModel.commandFinishExam.observe(viewLifecycleOwner) { attemptId ->
            findNavController().navigate(
                R.id.action_examFragment_to_resultFragment,
                ResultLauncher(attemptId).toBundleArgs()
            )
        }
    }

    private fun initViewPager() {
        binding.viewPagerQuestions.adapter = QuestionPagerAdapter(this, viewModel.attemptId)

        TabLayoutMediator(binding.tabLayoutNumbers, binding.viewPagerQuestions) { tab, position ->
            tab.text = position.plus(1).toString()
            tab.view.tag = pagerAdapter.getItemAt(position)
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
                val questionId = child?.tag as? String ?: return
                registerTabSelectionListener(questionId, child)
            }

            override fun onChildViewRemoved(parent: View?, child: View?) = Unit
        })
    }

    private fun registerTabSelectionListener(questionId: String, view: View) {
        val job = viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.questionSelection(questionId).collect { isSelected ->
                    view.setBackgroundResource(
                        if (isSelected) {
                            R.drawable.selector_tab_background_selected
                        } else {
                            R.drawable.selector_tab_background
                        }
                    )
                }
            }
        }
        questionSelectionJobs.add(job)
    }

    private fun initClickListeners() = with(binding) {
        btnSubmit.clickWithDebounce(action = viewModel::onSubmitClicked)
    }

    override fun onStart() {
        super.onStart()
        viewModel.onScreenVisible()
    }

    override fun onStop() {
        super.onStop()
        viewModel.onScreenHid()
    }
}