package io.flaterlab.meshexam.feature.meshroom

import android.os.Bundle
import android.view.View
import dagger.hilt.android.AndroidEntryPoint
import io.flaterlab.meshexam.androidbase.ViewBindingFragment
import io.flaterlab.meshexam.androidbase.ViewBindingProvider
import io.flaterlab.meshexam.feature.meshroom.databinding.FragmentMeshRoomBinding

@AndroidEntryPoint
internal class MeshRoomFragment : ViewBindingFragment<FragmentMeshRoomBinding>() {

    override val viewBinder: ViewBindingProvider<FragmentMeshRoomBinding>
        get() = FragmentMeshRoomBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}