package hr.stipanic.tomislav.thrifty.ui.profile_screen.edit_screen

import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDialogFragment
import hr.stipanic.tomislav.thrifty.R
import hr.stipanic.tomislav.thrifty.utils.BooleanListener
import kotlinx.android.synthetic.main.dialog_delete.*
import kotlinx.android.synthetic.main.dialog_delete.buttonDeleteNo
import kotlinx.android.synthetic.main.dialog_logout.*

class LogoutDialog : AppCompatDialogFragment() {

    lateinit var listener: BooleanListener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_logout)
        dialog.setCanceledOnTouchOutside(true)
        dialog.buttonLogoutNo.setOnClickListener{dismiss()}
        dialog.buttonLogoutYes.setOnClickListener{
            listener.onYes()
            dismiss()
        }
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.CENTER
        dialog.window!!.attributes = lp
        return dialog
    }

    companion object {
        fun newInstance(listener: BooleanListener) = LogoutDialog().apply {
            this.listener = listener
        }
    }
}