package hr.stipanic.tomislav.thrifty.ui.base_fragment

import android.os.Bundle
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import hr.stipanic.tomislav.thrifty.utils.popBackStackAllInstances

open class BaseBottomTabFragment : Fragment() {
    private var isNavigated = false

    fun navigateWithAction(action: NavDirections) {
        isNavigated = true
        findNavController().navigate(action)
    }

    fun navigate(resId: Int) {
        isNavigated = true
        findNavController().navigate(resId)
    }

    fun navigateWithBundle(resId: Int, bundle: Bundle) {
        isNavigated = true
        findNavController().navigate(resId, bundle)
    }



    override fun onDestroyView() {
        super.onDestroyView()
        if (!isNavigated)
            requireActivity().onBackPressedDispatcher.addCallback(this) {
                val navController = findNavController()
                if (navController.currentBackStackEntry?.destination?.id != null) {
                    findNavController().popBackStackAllInstances(
                        navController.currentBackStackEntry?.destination?.id!!,
                        true
                    )
                } else
                    navController.popBackStack()
            }
    }
}