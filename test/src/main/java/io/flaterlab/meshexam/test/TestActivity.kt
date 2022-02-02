package io.flaterlab.meshexam.test

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import io.flaterlab.meshexam.test.adapter.PagerAdapter
import io.flaterlab.meshexam.test.fragment.ClientMeshFragment
import io.flaterlab.meshexam.test.fragment.HostMeshFragment
import kotlinx.android.synthetic.main.activity_test.*

class TestActivity : AppCompatActivity() {

    private val request = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        if (
            permissions.any {
                ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
            }
        ) {
            request.launch(permissions)
        }
        test_container.adapter = PagerAdapter(
            listOf(
                ClientMeshFragment(),
                HostMeshFragment()
            ),
            this
        )
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
    }
}