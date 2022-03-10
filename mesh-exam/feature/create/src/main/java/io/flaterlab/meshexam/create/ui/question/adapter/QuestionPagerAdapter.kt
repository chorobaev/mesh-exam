package io.flaterlab.meshexam.create.ui.question.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import io.flaterlab.meshexam.create.ui.details.QuestionDetailsFragment
import io.flaterlab.meshexam.create.ui.details.QuestionDetailsLauncher
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

    fun addAll(ids: List<String>) {
        val positionStart = questionIds.size
        questionIds.addAll(ids)
        notifyItemRangeInserted(positionStart, ids.size)
    }

    fun addQuestion(id: String) {
        questionIds.add(id)
        notifyItemInserted(questionIds.lastIndex)
    }

    override fun createFragment(position: Int): Fragment {
        return QuestionDetailsFragment(
            QuestionDetailsLauncher(
                questionIds[position]
            )
        )
    }

    override fun getItemCount(): Int {
        return questionIds.size
    }
}