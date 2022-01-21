package io.flaterlab.meshexam.feature.main.message

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import io.flaterlab.meshexam.feature.main.NearbyViewModel
import io.flaterlab.meshexam.feature.main.databinding.FragmentMessageBinding
import javax.inject.Inject

@AndroidEntryPoint
class MessageFragment : Fragment() {

    private val viewModel: NearbyViewModel by activityViewModels()

    private var _binding: FragmentMessageBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var messageAdapter: MessageAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMessageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding.recyclerMessages) {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, true)
            adapter = messageAdapter
        }

        viewModel.connectedPhone.observe(viewLifecycleOwner) {
            if (it == null) {
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
        viewModel.messages.observe(viewLifecycleOwner, messageAdapter::submitList)

        binding.btnSend.setOnClickListener {
            val msg = binding.etMessage.text?.toString()
            if (!msg.isNullOrBlank()) {
                viewModel.onSendClicked(msg)
                binding.etMessage.text.clear()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}