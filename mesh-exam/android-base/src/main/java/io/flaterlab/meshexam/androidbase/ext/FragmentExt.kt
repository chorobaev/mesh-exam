package io.flaterlab.meshexam.androidbase.ext

import android.content.DialogInterface
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import io.flaterlab.meshexam.androidbase.dailog.AlertDialogFragment

fun Fragment.showAlert(
    message: String,
    title: String = "",
    positive: String = "Ok",
    negative: String? = null,
    negativeCallback: (DialogInterface) -> Unit = { it.dismiss() },
    positiveCallback: (DialogInterface) -> Unit = { it.dismiss() },
    cancelable: Boolean = true,
) {
    showAlert(
        childFragmentManager,
        message,
        title,
        positive,
        negative,
        negativeCallback,
        positiveCallback,
        cancelable
    )
}

private fun showAlert(
    fragmentManager: FragmentManager,
    message: String,
    title: String = "",
    positive: String = "Ok",
    negative: String? = null,
    negativeCallback: (DialogInterface) -> Unit = { it.dismiss() },
    positiveCallback: (DialogInterface) -> Unit = { it.dismiss() },
    cancelable: Boolean = true,
) {
    fragmentManager.beginTransaction().add(
        AlertDialogFragment().apply {
            this.message = message
            this.title = title
            this.positive = positive
            this.negative = negative
            this.negativeCallback = negativeCallback
            this.positiveCallback = positiveCallback
            this.isCancelable = cancelable
        }, AlertDialogFragment::class.java.name
    ).commitAllowingStateLoss()
}