package hr.stipanic.tomislav.thrifty.ui.profile_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import hr.stipanic.tomislav.thrifty.R
import hr.stipanic.tomislav.thrifty.databinding.FragmentProfileBinding
import hr.stipanic.tomislav.thrifty.ui.base_fragment.BaseBottomTabFragment
import hr.stipanic.tomislav.thrifty.ui.feed_screen.recycler_adapter.MainFeedRecyclerAdapter
import hr.stipanic.tomislav.thrifty.ui.profile_screen.edit_screen.LogoutDialog
import hr.stipanic.tomislav.thrifty.utils.*
import hr.stipanic.tomislav.thrifty.utils.Constants.ARTICLE_ID_EXTRA
import hr.stipanic.tomislav.thrifty.utils.Constants.IS_LOGGED_IN
import hr.stipanic.tomislav.thrifty.utils.Constants.LOGIN
import hr.stipanic.tomislav.thrifty.viewmodels.ProfileViewModel
import kotlinx.android.synthetic.main.fragment_profile.*
import org.koin.android.viewmodel.ext.android.viewModel

class ProfileFragment : BaseBottomTabFragment() {

    private val viewModel by viewModel<ProfileViewModel>()
    private lateinit var navController: NavController
    private val adapter by lazy { MainFeedRecyclerAdapter({ onItemSelected(it) }, { onEditSelected(it) }, true) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentProfileBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        binding.profileRecyclerView.adapter = adapter
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navController = findNavController()

        profileToolbar.setOnMenuItemClickListener { p0 ->
            when (p0?.itemId) {
                else -> onLogout()
            }
        }
        observeData()
        subscribeObservers()
        initRecycler()
        fabAddItem.setOnClickListener { navController.navigate(R.id.action_profileFragment_to_addInfoFragment) }
    }

    private fun initRecycler() {
        profileRecyclerView.layoutManager = LinearLayoutManager(context)
    }


    private fun subscribeObservers() {
        viewModel.observeQuery().observe(viewLifecycleOwner, Observer<State> {
            if (it != null) {
                when (it) {
                    State.LOADING -> show(profileProgress)
                    State.SUCCESS -> hide(profileProgress)
                    State.ERROR -> hide(profileProgress)
                }
            }
        })
    }

    private fun observeData() {
        viewModel.getArticles().removeObservers(this)
        viewModel.getArticles().observe(viewLifecycleOwner, Observer { taskList ->
            run {
                adapter.addItems(taskList)
            }
        })

        viewModel.getUser().removeObservers(this)
        viewModel.getUser().observe(viewLifecycleOwner, Observer { user ->
            run {
                profileToolbar.title = String.format(requireContext().getString(R.string.profile_title), user.nickname)
            }
        })
    }

    private fun onLogout(): Boolean {

        LogoutDialog.newInstance(object : BooleanListener {
            override fun onYes() {
                showSnackBar("You have been logged out")
                activity?.getSharedPreferences(LOGIN, 0)?.edit()?.putBoolean(IS_LOGGED_IN, false)?.apply()
                navigate(R.id.action_profileFragment_to_loginFragment)
            }
        }).show(childFragmentManager, "logoutFragment")
        return true
    }


    private fun onItemSelected(id: String) {
        goToDetailsFragment(id)
    }

    private fun onEditSelected(id: String) {
        goToEditFragment(id)
    }

    private fun goToDetailsFragment(id: String) {
        navController.navigate(
            R.id.action_profileFragment_to_detailsFragment,
            bundleOf(Pair(ARTICLE_ID_EXTRA, id))
        )
    }

    private fun goToEditFragment(id: String) {
        navController.navigate(
            R.id.action_profileFragment_to_editFragment,
            bundleOf(Pair(ARTICLE_ID_EXTRA, id))
        )
    }
}