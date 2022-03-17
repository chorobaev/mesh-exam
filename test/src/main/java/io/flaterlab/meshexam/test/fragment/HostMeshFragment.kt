package io.flaterlab.meshexam.test.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import io.flaterlab.meshexam.librariy.mesh.common.dto.AdvertiserInfo
import io.flaterlab.meshexam.librariy.mesh.host.HostMeshManager
import io.flaterlab.meshexam.test.R
import kotlinx.android.synthetic.main.fragment_connect.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

class HostMeshFragment : Fragment() {

    private val mesh get() = HostMeshManager.getInstance(requireContext())

    private var isAdvertising = false
        set(value) {
            btn_advertise.text = if (value) "Advertising..." else "Advertise"
            field = value
        }
    private var job: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_connect, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_discover.isVisible = false
        btn_advertise.setOnClickListener {
            if (isAdvertising) {
                mesh.stop()
                job?.cancel()
                job = null
                isAdvertising = false
            } else {
                isAdvertising = true
                job = viewLifecycleOwner.lifecycleScope.launch {
                    mesh.create(
                        AdvertiserInfo(
                            "Nurbol Chorobaev",
                            "exam id",
                            "History",
                            30
                        )
                    )
                        .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                        .collectLatest { result ->
                            Timber.d("Clients: $result")
                            linear_layout.run {
                                removeAllViews()
                                result.clientList.forEach { clientInfo ->
                                    val text = layoutInflater.inflate(
                                        R.layout.item_text,
                                        this,
                                        false
                                    ) as TextView
                                    text.text = clientInfo.name
                                    addView(text)
                                }
                            }
                        }
                }
            }
        }
    }
}