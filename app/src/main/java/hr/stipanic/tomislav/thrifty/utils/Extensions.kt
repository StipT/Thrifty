package hr.stipanic.tomislav.thrifty.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.google.android.material.snackbar.Snackbar

fun NavController.popBackStackAllInstances(destination: Int, inclusive: Boolean): Boolean {
    var popped: Boolean
    while (true) {
        popped = popBackStack(destination, inclusive)
        if (!popped) {
            break
        }
    }
    return popped
}

fun Fragment.showSnackBar(message: String) {
    Snackbar.make(
        requireActivity().findViewById(android.R.id.content),
        message,
        Snackbar.LENGTH_SHORT
    ).show()
}


fun Fragment.hide(view: View) {
    view.visibility = View.GONE
}


fun Fragment.show(view: View) {
    view.visibility = View.VISIBLE
}

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

