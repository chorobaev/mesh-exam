package io.flaterlab.meshexam.result

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import io.flaterlab.meshexam.androidbase.BaseFragment
import io.flaterlab.meshexam.androidbase.getLauncher
import io.flaterlab.meshexam.androidbase.toBundleArgs

@AndroidEntryPoint
internal class ProxyFragment : BaseFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val launcher: ResultLauncher = getLauncher()
        findNavController().navigate(
            when (launcher) {
                is HostResultLauncher -> R.id.action_proxyFragment_to_resultListFragment
                is ClientResultLauncher -> R.id.action_proxyFragment_to_individualResultFragment
            },
            launcher.toBundleArgs()
        )
    }
}