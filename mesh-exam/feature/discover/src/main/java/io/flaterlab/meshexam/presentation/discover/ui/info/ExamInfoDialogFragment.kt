package io.flaterlab.meshexam.presentation.discover.ui.info

import android.os.Bundle
import android.view.View
import androidx.core.view.isInvisible
import androidx.fragment.app.FragmentManager
import dagger.hilt.android.AndroidEntryPoint
import io.flaterlab.meshexam.androidbase.ViewBindingBottomSheetDialogFragment
import io.flaterlab.meshexam.androidbase.ViewBindingProvider
import io.flaterlab.meshexam.androidbase.ext.clickWithDebounce
import io.flaterlab.meshexam.androidbase.setLauncher
import io.flaterlab.meshexam.androidbase.text.setText
import io.flaterlab.meshexam.presentation.discover.databinding.DialogExamInfoBinding
import io.flaterlab.meshexam.presentation.discover.databinding.ItemExamInfoBinding
import io.flaterlab.meshexam.presentation.discover.dvo.ExamInfoItemDvo
import io.flaterlab.meshexam.presentation.discover.router.DiscoverRouter
import javax.inject.Inject

@AndroidEntryPoint
internal class ExamInfoDialogFragment :
    ViewBindingBottomSheetDialogFragment<DialogExamInfoBinding> {

    @Deprecated(message = DEPRECATION_MESSAGE, level = DeprecationLevel.ERROR)
    constructor()

    constructor(launcher: ExamInfoLauncher) {
        setLauncher(launcher)
    }

    companion object {
        fun show(launcher: ExamInfoLauncher, fragmentManager: FragmentManager) {
            ExamInfoDialogFragment(launcher).show(
                fragmentManager,
                ExamInfoDialogFragment::class.java.canonicalName
            )
        }
    }

    @Inject
    lateinit var discoverRouter: DiscoverRouter

    private val viewModel: ExamInfoViewModel by vm()

    override val viewBinder: ViewBindingProvider<DialogExamInfoBinding>
        get() = DialogExamInfoBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.examName.observe(viewLifecycleOwner, binding.tvExamName::setText)
        viewModel.examInfoItemList.observe(viewLifecycleOwner, ::onInfoItemsReceived)
        viewModel.commandEditClient.observe(viewLifecycleOwner) { discoverRouter.openEditProfile(it) }
        viewModel.commandJoinExam.observe(viewLifecycleOwner) { examId ->
            dismiss()
            discoverRouter.joinExam(examId)
        }

        binding.btnJoinExam.clickWithDebounce(action = viewModel::onJoinClicked)
    }

    private fun onInfoItemsReceived(items: List<ExamInfoItemDvo>) =
        with(binding.containerExamInfoItems) {
            removeAllViews()
            items.forEach { exam ->
                addView(createExamInfoItemView(exam))
            }
        }

    private fun createExamInfoItemView(item: ExamInfoItemDvo): View {
        return ItemExamInfoBinding.inflate(layoutInflater)
            .apply {
                tvExamInfoItemTitle.setText(item.title)
                tvExamInfoItemValue.setText(item.value)
                btnExamInfoEdit.isInvisible = !item.isEditable
                btnExamInfoEdit.clickWithDebounce(action = item.onEdit)
            }
            .root
    }
}