package io.flaterlab.meshexam.androidbase.dailog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class AlertDialogFragment : DialogFragment() {

    var message: String = ""
    var title: String = ""
    var positive: String = ""
    var negative: String? = null
    var negativeCallback: (DialogInterface) -> Unit = { it.dismiss() }
    var positiveCallback: (DialogInterface) -> Unit = { it.dismiss() }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val adb = AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setPositiveButton(positive) { dialog, _ ->
                positiveCallback(dialog)
            }

        if (message.isNotEmpty()) {
            adb.setMessage(message)
        }

        if (negative != null) {
            adb.setNegativeButton(negative) { dialog, _ ->
                negativeCallback(dialog)
            }
        }

        return adb.create()
    }
}
