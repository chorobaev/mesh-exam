package io.flaterlab.meshexam.result.ui.result.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import io.flaterlab.meshexam.result.dvo.ResultQuestionInfoDvo
import io.flaterlab.meshexam.result.ui.question.ResultQuestionFragment
import io.flaterlab.meshexam.result.ui.question.ResultQuestionLauncher
import javax.inject.Inject

internal class ResultQuestionPagerAdapter @Inject constructor(
    fragment: Fragment,
) : FragmentStateAdapter(fragment) {

    private var list: List<ResultQuestionInfoDvo> = emptyList()

    fun getItemAt(position: Int): ResultQuestionInfoDvo = list[position]

    fun submitList(list: List<ResultQuestionInfoDvo>) {
        notifyItemRangeRemoved(0, list.size)
        this.list = list
        notifyItemRangeInserted(0, list.size)
    }

    override fun createFragment(position: Int): Fragment {
        val dvo = list[position]
        return ResultQuestionFragment(
            ResultQuestionLauncher(dvo.questionId, dvo.attemptId, dvo.showCorrectness)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }
}