package hr.stipanic.tomislav.thrifty

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private var selectedItem: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_main)
        setupNav()
    }


    private fun setupNav() {
        navController = findNavController(R.id.nav_host_fragment)
        bottomNavigationView.setupWithNavController(navController)
        selectedItem = bottomNavigationView.selectedItemId
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.loginFragment -> {
                    hideBottomNav()
                    adjustForSoftKeyboard()
                }
                R.id.registerFragment -> {
                    hideBottomNav()
                    adjustForSoftKeyboard()
                }
                R.id.splashFragment -> {
                    hideBottomNav()
                    adjustNothing()
                }
                R.id.addInfoFragment -> {
                    hideBottomNav()
                    adjustForSoftKeyboard()
                }
                R.id.addLocationFragment -> {
                    hideBottomNav()
                    adjustNothing()
                }
                R.id.addImageFragment -> {
                    hideBottomNav()
                    adjustNothing()
                }
                R.id.chatFragment -> {
                    hideBottomNav()
                    adjustForSoftKeyboard()
                    resizeForSoftKeyboard()
                }
                R.id.editFragment -> {
                    hideBottomNav()
                    adjustForSoftKeyboard()
                    resizeForSoftKeyboard()
                }
                else -> {
                    showBottomNav()
                    adjustNothing()
                }
            }
        }
    }

    private fun showBottomNav() {
        bottomNavigationView.visibility = View.VISIBLE
        navBarBorder.visibility = View.VISIBLE

    }

    private fun hideBottomNav() {
        bottomNavigationView.visibility = View.GONE
        navBarBorder.visibility = View.GONE
    }

    private fun adjustForSoftKeyboard() {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
    }
    private fun resizeForSoftKeyboard() {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }
    private fun adjustNothing() {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
    }
}