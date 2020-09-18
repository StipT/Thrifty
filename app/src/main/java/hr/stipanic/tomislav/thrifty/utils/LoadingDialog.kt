package hr.stipanic.tomislav.thrifty.utils

import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDialogFragment
import hr.stipanic.tomislav.thrifty.R

class LoadingDialog : AppCompatDialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_loading)
        dialog.setCanceledOnTouchOutside(false)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.CENTER
        dialog.window!!.attributes = lp
        return dialog
    }
}