package hr.stipanic.tomislav.thrifty.ui.feed_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionInflater
import hr.stipanic.tomislav.thrifty.R
import hr.stipanic.tomislav.thrifty.databinding.FragmentDetailsBinding
import hr.stipanic.tomislav.thrifty.utils.Constants.ARTICLE_ID_EXTRA
import hr.stipanic.tomislav.thrifty.utils.Constants.USER_ID_EXTRA
import hr.stipanic.tomislav.thrifty.utils.StringUtils
import hr.stipanic.tomislav.thrifty.viewmodels.DetailsViewModel
import kotlinx.android.synthetic.main.fragment_details.*
import org.koin.android.viewmodel.ext.android.viewModel

class DetailsFragment : Fragment() {

    private val viewModel by viewModel<DetailsViewModel>()
    private lateinit var navController: NavController
    private lateinit var articleId: String
    private lateinit var userId: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        articleId = requireArguments().getString(ARTICLE_ID_EXTRA)!!

        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        val binding: FragmentDetailsBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false)
        observeData(binding)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navController = findNavController()
        editToolbar.navigationIcon = ResourcesCompat.getDrawable(
            resources,
            R.drawable.ic_baseline_arrow_back_24,
            null
        )
        deleteButton.setOnClickListener { onMessageClick() }
        editToolbar.setNavigationOnClickListener { navController.popBackStack() }
    }

    private fun onMessageClick() {
        navController.navigate(
            R.id.action_detailsFragment_to_chatFragment,
            bundleOf(Pair(ARTICLE_ID_EXTRA, articleId), Pair(USER_ID_EXTRA, userId))
        )
    }

    private fun observeData(binding: FragmentDetailsBinding) {
        viewModel.getArticle(articleId).removeObservers(this)
        viewModel.getArticle(articleId).observe(viewLifecycleOwner, Observer { task ->
            run {
                binding.article = task
                editToolbar.title = task.title

                viewModel.getUser(task.userId).removeObservers(this)
                viewModel.getUser(task.userId).observe(viewLifecycleOwner, Observer { task ->
                    run {
                        userId = task.userId
                        deleteButton.isEnabled = true
                        binding.user = task
                        binding.stringUtils = StringUtils
                    }
                })
            }
        })
    }
}