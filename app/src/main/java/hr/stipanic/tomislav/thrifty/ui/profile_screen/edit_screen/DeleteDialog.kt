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

class DeleteDialog : AppCompatDialogFragment() {

    lateinit var listener: BooleanListener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_delete)
        dialog.setCanceledOnTouchOutside(true)
        dialog.buttonDeleteNo.setOnClickListener{dismiss()}
        dialog.buttonDeleteYes.setOnClickListener{
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
        fun newInstance(listener: BooleanListener) = DeleteDialog().apply {
            this.listener = listener
        }
    }
}