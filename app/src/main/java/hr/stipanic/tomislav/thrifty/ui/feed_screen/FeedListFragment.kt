package hr.stipanic.tomislav.thrifty.ui.feed_screen

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.os.bundleOf
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import hr.stipanic.tomislav.thrifty.R
import hr.stipanic.tomislav.thrifty.databinding.FragmentFeedListBinding
import hr.stipanic.tomislav.thrifty.ui.feed_screen.recycler_adapter.MainFeedRecyclerAdapter
import hr.stipanic.tomislav.thrifty.utils.Constants.ARTICLE_ID_EXTRA
import hr.stipanic.tomislav.thrifty.utils.Constants.CATEGORY_EXTRA
import hr.stipanic.tomislav.thrifty.utils.State
import hr.stipanic.tomislav.thrifty.utils.hide
import hr.stipanic.tomislav.thrifty.utils.show
import hr.stipanic.tomislav.thrifty.viewmodels.FeedListViewModel
import kotlinx.android.synthetic.main.fragment_feed_list.*
import org.koin.android.viewmodel.ext.android.viewModel

class FeedListFragment : Fragment() {

    private val viewModel by viewModel<FeedListViewModel>()
    private lateinit var navController: NavController
    lateinit var category: String
    private val adapter by lazy { MainFeedRecyclerAdapter({ onItemSelected(it) }, {}, false) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentFeedListBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_feed_list, container, false)
        binding.feedRecyclerView.adapter = adapter
        return binding.root
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        category = requireArguments().getString(CATEGORY_EXTRA)!!
        feedToolbar.title = category
        feedToolbar.navigationIcon = resources.getDrawable(
            R.drawable.ic_baseline_arrow_back_24,
            null
        )
        feedToolbar.setNavigationOnClickListener { navController.popBackStack() }
        initRecycler()
        observeData()
        setSearchView()
        subscribeObservers()
    }

    private fun initRecycler() {
        feedRecyclerView.layoutManager = LinearLayoutManager(context)
    }


    private fun observeData() {
        viewModel.getArticles(category).removeObservers(this)
        viewModel.getArticles(category).observe(viewLifecycleOwner, Observer { taskList ->
            run {
                adapter.addItems(taskList)
            }
        })
    }

    private fun subscribeObservers() {
        viewModel.observeQuery().observe(viewLifecycleOwner, Observer<State> {
            if (it != null) {
                when (it) {
                    State.LOADING -> show(feedListProgress)
                    State.SUCCESS -> hide(feedListProgress)
                    State.ERROR -> hide(feedListProgress)
                }
            }
        })
    }

    private fun setSearchView() {
        val searchView = feedToolbar.menu[0].actionView as SearchView
        searchView.imeOptions = EditorInfo.IME_ACTION_DONE
        searchView.queryHint = getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                adapter.filter!!.filter(newText)
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }
        })
    }

    private fun onItemSelected(id: String) {
        goToDetailsFragment(id)
    }

    private fun goToDetailsFragment(id: String) {
        navController.navigate(
            R.id.action_feedListFragment_to_detailsFragment,
            bundleOf(Pair(ARTICLE_ID_EXTRA, id))
        )
    }
}