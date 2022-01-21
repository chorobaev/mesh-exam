package io.flaterlab.meshexam.feature.main.home

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import dagger.hilt.android.AndroidEntryPoint
import io.flaterlab.meshexam.feature.main.NearbyViewModel
import io.flaterlab.meshexam.feature.main.R
import io.flaterlab.meshexam.feature.main.databinding.FragmentHomeBinding
import io.flaterlab.meshexam.feature.main.databinding.ItemHostBinding
import io.flaterlab.meshexam.feature.main.message.MessageFragment

@AndroidEntryPoint
class HomeFragment : Fragment() {

    companion object {
        private val permissions = buildList {
            add(Manifest.permission.ACCESS_FINE_LOCATION)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                add(Manifest.permission.BLUETOOTH_ADVERTISE)
                add(Manifest.permission.BLUETOOTH_CONNECT)
                add(Manifest.permission.BLUETOOTH_SCAN)
            }
        }.toTypedArray()
    }

    private val viewModel: NearbyViewModel by activityViewModels()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val permissionRequestRegister = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { results ->
        if (results.all { (_, isGranted) -> isGranted }) {
            checkPermissions()
        }
    }
    private var nameTextWatcher: TextWatcher? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermissions()
        viewModel.advertising.observe(viewLifecycleOwner) { advertising ->
            binding.btnAdvertise.text = if (advertising) {
                "Stop advertising"
            } else {
                "Advertise"
            }
        }
        viewModel.discovering.observe(viewLifecycleOwner) { discovering ->
            binding.btnScan.text = if (discovering) {
                "Stop discovering"
            } else {
                "Discover"
            }
        }
        viewModel.availableList.observe(viewLifecycleOwner) { list ->
            binding.foundItems.removeAllViews()
            list.forEach { item ->
                ItemHostBinding.inflate(layoutInflater).root.also { view ->
                    view.text = item.name
                    view.tag = item.hostId
                    view.setOnClickListener {
                        viewModel.onItemClicked(item)
                    }
                    binding.foundItems.addView(view)
                }
            }
        }
        viewModel.nameEntered.observe(viewLifecycleOwner) {
            checkPermissions()
        }
        viewModel.connectedPhone.observe(viewLifecycleOwner) { phoneId ->
            if (phoneId != null) {
                requireActivity().supportFragmentManager.commit {
                    replace(R.id.main_fragment_container, MessageFragment())
                    addToBackStack(HomeFragment::class.java.canonicalName)
                }
            }
        }
        binding.btnAdvertise.setOnClickListener {
            viewModel.onAdvertiseClick()
        }
        binding.btnScan.setOnClickListener {
            viewModel.onDiscoverClicked()
        }
        nameTextWatcher = binding.etName.addTextChangedListener { name ->
            name?.toString()?.let(viewModel::onNameChanged)
        }
    }

    private fun checkPermissions() {
        val isGranted = isPermissionsGranted()
        binding.btnAdvertise.isEnabled = isGranted && viewModel.nameEntered.value == true
        binding.btnScan.isEnabled = isGranted && viewModel.nameEntered.value == true
        if (!isGranted) {
            permissionRequestRegister.launch(permissions)
        }
    }

    private fun isPermissionsGranted(): Boolean {
        return permissions.all { permission ->
            ContextCompat.checkSelfPermission(requireContext(), permission) ==
                    PackageManager.PERMISSION_GRANTED
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.etName.removeTextChangedListener(nameTextWatcher)
        _binding = null
    }
}