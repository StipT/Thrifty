package hr.stipanic.tomislav.thrifty.ui.splash_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import hr.stipanic.tomislav.thrifty.R
import hr.stipanic.tomislav.thrifty.utils.Constants.IS_LOGGED_IN
import hr.stipanic.tomislav.thrifty.utils.Constants.LOGIN

class SplashFragment : Fragment() {

    private lateinit var navController: NavController

    private fun isLoggedIn(): Boolean {
        val sharedPreferences = requireContext().getSharedPreferences(LOGIN, 0)
        return sharedPreferences.getBoolean(IS_LOGGED_IN, false)
    }


    private fun goToCategoryFragment() {
        navController.navigate(R.id.action_splashFragment_to_categoryFragment)
    }

    private fun goToAuthFragment() {
        navController.navigate(R.id.action_splashFragment_to_loginFragment)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       return inflater.inflate(R.layout.fragment_splash, container, false )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        if (isLoggedIn()) {
            goToCategoryFragment()
        } else {
            goToAuthFragment()
        }
    }
}