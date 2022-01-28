package io.flaterlab.meshexam.create.ui.question

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.children
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import io.flaterlab.meshexam.androidbase.ViewBindingFragment
import io.flaterlab.meshexam.androidbase.ViewBindingProvider
import io.flaterlab.meshexam.androidbase.ext.applyLayoutParams
import io.flaterlab.meshexam.androidbase.ext.clickWithDebounce
import io.flaterlab.meshexam.create.R
import io.flaterlab.meshexam.create.databinding.FragmentCreateQuestionBinding
import io.flaterlab.meshexam.create.ui.question.adapter.QuestionPagerAdapter
import javax.inject.Inject

@AndroidEntryPoint
internal class CreateQuestionFragment : ViewBindingFragment<FragmentCreateQuestionBinding>() {

    private val viewModel: CreateQuestionViewModel by viewModels()

    @Inject
    lateinit var pagerAdapter: QuestionPagerAdapter

    override val viewBinder: ViewBindingProvider<FragmentCreateQuestionBinding>
        get() = FragmentCreateQuestionBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewPager()
        initTabStyler()

        viewModel.questionMetaInfo.observe(viewLifecycleOwner) { meta ->
            binding.toolbarCreateQuestion.run {
                setTitle(meta.name)
                setSubtitle(meta.type)
                showSubtitle()
            }
        }
        viewModel.questionIds.observe(viewLifecycleOwner) { ids ->
            pagerAdapter.submitList(ids)
            viewModel.questionIds.removeObservers(viewLifecycleOwner)
        }
        viewModel.questionIdAdded.observe(viewLifecycleOwner) { id ->
            pagerAdapter.addQuestion(id)
            binding.viewPagerQuestions.setCurrentItem(pagerAdapter.itemCount - 1, false)
            binding.questionNumbers.post {
                binding.questionNumbers.fullScroll(View.FOCUS_RIGHT)
            }
        }

        binding.tvAddQuestion.clickWithDebounce(action = viewModel::onAddQuestionClicked)
    }

    private fun initViewPager() {
        binding.viewPagerQuestions.adapter = pagerAdapter

        TabLayoutMediator(binding.tabLayoutNumber, binding.viewPagerQuestions) { tab, position ->
            tab.text = position.plus(1).toString()
        }.attach()

        binding.tabLayoutNumber.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            private val size: Float

            init {
                val tabWidth = resources.getDimension(R.dimen.tab_side)
                val displayWidth = resources.displayMetrics.widthPixels
                size = (displayWidth - tabWidth) / 2
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab == null) return
                val scrollToX = tab.view.x - size
                binding.questionNumbers.smoothScrollTo(scrollToX.toInt(), 0)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) = Unit
            override fun onTabReselected(tab: TabLayout.Tab?) = Unit
        })
    }

    private fun initTabStyler() {
        val side = resources.getDimension(R.dimen.tab_side).toInt()
        val margin = resources.getDimension(R.dimen.margin_medium).toInt()
        val tabContainer = binding.tabLayoutNumber.children.first() as ViewGroup
        tabContainer.setOnHierarchyChangeListener(object : ViewGroup.OnHierarchyChangeListener {
            override fun onChildViewAdded(parent: View?, child: View?) {
                child?.applyLayoutParams<LinearLayout.LayoutParams> {
                    weight = 0F
                    height = side
                    width = side
                    marginStart = margin
                }
                binding.tabLayoutNumber.requestLayout()
            }

            override fun onChildViewRemoved(parent: View?, child: View?) = Unit
        })
    }
}