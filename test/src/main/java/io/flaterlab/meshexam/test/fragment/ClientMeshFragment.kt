package io.flaterlab.meshexam.test.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import io.flaterlab.meshexam.librariy.mesh.client.ClientMeshManager
import io.flaterlab.meshexam.librariy.mesh.common.dto.ClientInfo
import io.flaterlab.meshexam.test.R
import io.flaterlab.meshexam.test.adapter.TextAdapter
import kotlinx.android.synthetic.main.fragment_list_sith_button.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*

class ClientMeshFragment : Fragment() {

    private val clientMesh
        get() = ClientMeshManager.getInstance(requireContext())
    private val clientInfo = ClientInfo(UUID.randomUUID().toString(), "Nurbol", "COM-18", 0)

    private var isDiscovering = false
        set(value) {
            btn_action.text = if (value) "Discovering..." else "Discover"
            field = value
        }
    private var discoveryJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list_sith_button, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = TextAdapter()
        adapter.textClickListener = {
            viewLifecycleOwner.lifecycleScope.launch {
                try {
                    val info = clientMesh.joinExam(it, clientInfo)
                    Toast.makeText(requireContext(), "Connected to $info", Toast.LENGTH_SHORT)
                        .show()
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
        recycler_view.adapter = adapter
        btn_action.text = "Discover"

        btn_action.setOnClickListener {
            if (isDiscovering) {
                discoveryJob?.cancel()
                isDiscovering = false
            } else {
                isDiscovering = true
                discoveryJob = viewLifecycleOwner.lifecycleScope.launch {
                    clientMesh.discoverExams()
                        .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                        .collectLatest { result ->
                            adapter.submitList(
                                result.advertiserList.map { it.examId }
                            )
                        }
                }
            }
        }
    }
}