package hr.stipanic.tomislav.thrifty.ui.chat_screen.recycler_adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import hr.stipanic.tomislav.thrifty.data.model.Message
import hr.stipanic.tomislav.thrifty.databinding.MessageItemOutBinding
import hr.stipanic.tomislav.thrifty.utils.StringUtils

class OutMessageViewHolder(private val messageItemOutBinding: MessageItemOutBinding) :
    RecyclerView.ViewHolder(messageItemOutBinding.root) {

    fun onBind(messageList: List<Message>, position: Int) {
        if(position != 0 && messageList[position - 1].userId == messageList[position].userId) {
            messageItemOutBinding.chatNicknameOut.visibility = View.GONE
        } else {
            messageItemOutBinding.chatNicknameOut.visibility = View.VISIBLE
        }
        messageItemOutBinding.message = messageList[position]
        messageItemOutBinding.stringUtils = StringUtils
        messageItemOutBinding.executePendingBindings()
    }
}