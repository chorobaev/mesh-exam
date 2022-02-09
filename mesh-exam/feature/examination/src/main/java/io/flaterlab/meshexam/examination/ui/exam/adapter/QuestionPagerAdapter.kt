package io.flaterlab.meshexam.examination.ui.exam.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import io.flaterlab.meshexam.examination.ui.question.QuestionFragment
import io.flaterlab.meshexam.examination.ui.question.QuestionLauncher
import javax.inject.Inject

internal class QuestionPagerAdapter @Inject constructor(
    fragment: Fragment,
) : FragmentStateAdapter(fragment) {

    private val questionIds = ArrayList<String>()

    fun submitList(ids: List<String>) {
        val itemCount = questionIds.size
        questionIds.clear()
        questionIds.addAll(ids)
        notifyItemRangeRemoved(0, itemCount)
        notifyItemRangeInserted(0, ids.size)
    }

    override fun createFragment(position: Int): Fragment {
        return QuestionFragment(
            QuestionLauncher(
                questionIds[position]
            )
        )
    }

    override fun getItemCount(): Int {
        return questionIds.size
    }
}