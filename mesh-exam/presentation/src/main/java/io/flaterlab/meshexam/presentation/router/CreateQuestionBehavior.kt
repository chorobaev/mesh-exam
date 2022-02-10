package io.flaterlab.meshexam.presentation.router

import androidx.fragment.app.Fragment
import io.flaterlab.meshexam.androidbase.toBundleArgs
import io.flaterlab.meshexam.create.ui.question.CreateQuestionActionBehavior
import io.flaterlab.meshexam.feature.meshroom.MeshRoomLauncher
import io.flaterlab.meshexam.permission.MeshPermissionDialogFragment
import io.flaterlab.meshexam.presentation.R
import io.flaterlab.meshexam.presentation.di.FragmentEntryPoint
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
class CreateQuestionBehavior(
    private val examId: String,
) : CreateQuestionActionBehavior {

    @IgnoredOnParcel
    override val actionTitleResId: Int = R.string.mesh_create_mesh

    override fun onActionClicked(fragment: Fragment) {
        if (MeshPermissionDialogFragment.isMeshPermissionsGranted(fragment.requireContext())) {
            openMeshRoom(fragment)
        } else {
            MeshPermissionDialogFragment.show(fragment.childFragmentManager) { granted ->
                if (granted) openMeshRoom(fragment)
            }
        }
    }

    private fun openMeshRoom(fragment: Fragment) {
        FragmentEntryPoint.resolve(fragment)
            .globalNavControllerProvider
            .get()
            .navigate(
                R.id.action_global_nav_mesh,
                MeshRoomLauncher(examId).toBundleArgs()
            )
    }
}