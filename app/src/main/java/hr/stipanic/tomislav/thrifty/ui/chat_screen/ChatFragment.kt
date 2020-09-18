package hr.stipanic.tomislav.thrifty.ui.chat_screen

import android.R.attr
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import hr.stipanic.tomislav.thrifty.R
import hr.stipanic.tomislav.thrifty.databinding.FragmentChatBinding
import hr.stipanic.tomislav.thrifty.ui.chat_screen.recycler_adapter.MessageRecyclerAdapter
import hr.stipanic.tomislav.thrifty.utils.Constants.ARTICLE_ID_EXTRA
import hr.stipanic.tomislav.thrifty.utils.State
import hr.stipanic.tomislav.thrifty.utils.hide
import hr.stipanic.tomislav.thrifty.utils.hideKeyboard
import hr.stipanic.tomislav.thrifty.utils.show
import hr.stipanic.tomislav.thrifty.viewmodels.ChatViewModel
import kotlinx.android.synthetic.main.fragment_chat.*
import org.koin.android.viewmodel.ext.android.viewModel


class ChatFragment : Fragment() {

    private val viewModel by viewModel<ChatViewModel>()
    private lateinit var navController: NavController
    lateinit var userId: String
    lateinit var articleId: String
    private val adapter by lazy { MessageRecyclerAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        articleId = requireArguments().getString(ARTICLE_ID_EXTRA)!!
        val binding: FragmentChatBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_chat, container, false)
        binding.chatRecyclerView.adapter = adapter
        observeUser()
        return binding.root
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        chatToolbar.title = "Chat"
        chatToolbar.navigationIcon = resources.getDrawable(
            R.drawable.ic_baseline_arrow_back_24,
            null
        )
        chatToolbar.setNavigationOnClickListener { navController.popBackStack() }
        sendButton.setOnClickListener { sendMessage() }

        initRecycler()
        setImeAction()
        subscribeObservers()
        observeMessages()
    }

    private fun setImeAction() {
        enteMessageEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                sendMessage()
                true
            } else false
        }
    }

    private fun sendMessage() {
        viewModel.sendMessage(enteMessageEditText.text.trim().toString())
        enteMessageEditText.text.clear()
        hideKeyboard()
    }

    private fun initRecycler() {
        chatRecyclerView.layoutManager = LinearLayoutManager(context)

        chatRecyclerView.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            if (attr.bottom > oldBottom) {
                println("OVDJE SAM")
                chatRecyclerView.postDelayed(
                    Runnable
                    {
                        chatRecyclerView.smoothScrollToPosition((adapter.itemCount))
                    }, 100
                )
            }
        }
    }


    private fun observeUser() {
        viewModel.getCurrentUser().removeObservers(this)
        viewModel.getCurrentUser().observe(viewLifecycleOwner, Observer { user ->
            run {
                adapter.setUserId(user.userId)
            }
        })

    }

    private fun observeMessages() {
        viewModel.getMessages(articleId).removeObservers(this)
        viewModel.getMessages(articleId)
            .observe(viewLifecycleOwner, Observer { messageList ->
                run {
                    adapter.addItems(messageList)
                }
            })
    }

    private fun subscribeObservers() {
        viewModel.observeQuery().observe(viewLifecycleOwner, Observer<State> {
            if (it != null) {
                when (it) {
                    State.LOADING -> show(chatProgress)
                    State.SUCCESS -> hide(chatProgress)
                    State.ERROR -> hide(chatProgress)
                }
            }
        })

        viewModel.observeSend().observe(viewLifecycleOwner, Observer<State> {
            if (it != null) {
                when (it) {
                    State.LOADING -> show(chatProgress)
                    State.SUCCESS -> {
                        observeMessages()
                        hide(chatProgress)
                    }
                    State.ERROR -> hide(chatProgress)
                }
            }
        })
    }
}