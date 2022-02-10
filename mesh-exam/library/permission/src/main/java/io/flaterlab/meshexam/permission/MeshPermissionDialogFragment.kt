package io.flaterlab.meshexam.permission

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import dagger.hilt.android.AndroidEntryPoint
import io.flaterlab.meshexam.androidbase.ext.showAlert

@AndroidEntryPoint
class MeshPermissionDialogFragment : DialogFragment() {

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
            if (result.all { it.value }) onSuccess() else processDecline()
        }

    private val requestPermissionInSettingsLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (isMeshPermissionsGranted(requireContext())) onSuccess() else onFailure()
        }

    private var onResult: (Boolean) -> Unit = {}

    override fun onStart() {
        super.onStart()
        requestPermissions()
    }

    private fun requestPermissions() {
        if (!isMeshPermissionsGranted(requireContext())) {
            requestPermissionLauncher.launch(permissions)
        } else {
            dismiss()
            onSuccess()
        }
    }

    private fun processDecline() {
        val isDoNotAskFlagSet =
            permissions.any { permission ->
                !shouldShowRequestPermissionRationale(permission)
            }
        if (isDoNotAskFlagSet) {
            showAlert(
                message = getString(R.string.permission_mesh_message),
                positive = getString(R.string.common_allow),
                negative = getString(R.string.common_cancel),
                positiveCallback = { openAppPermissionSettings() },
                negativeCallback = { onFailure() }
            )
        } else {
            onFailure()
        }
    }

    private fun openAppPermissionSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri: Uri = Uri.fromParts("package", requireContext().packageName, null)
        intent.data = uri
        requestPermissionInSettingsLauncher.launch(intent)
    }

    private fun onSuccess() {
        dismiss()
        onResult(true)
    }

    private fun onFailure() {
        dismiss()
        onResult(false)
    }

    companion object {
        private val permissions = buildList {
            add(Manifest.permission.ACCESS_FINE_LOCATION)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                add(Manifest.permission.BLUETOOTH_ADVERTISE)
                add(Manifest.permission.BLUETOOTH_CONNECT)
                add(Manifest.permission.BLUETOOTH_SCAN)
            }
        }.toTypedArray()

        fun show(
            fragmentManager: FragmentManager,
            onResult: (Boolean) -> Unit,
        ) {
            MeshPermissionDialogFragment()
                .apply { this.onResult = onResult }
                .show(
                    fragmentManager,
                    MeshPermissionDialogFragment::class.java.canonicalName
                )
        }

        fun isMeshPermissionsGranted(context: Context): Boolean = permissions.all { permission ->
            ContextCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        }
    }
}