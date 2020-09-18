package hr.stipanic.tomislav.thrifty.ui.chat_screen.recycler_adapter


import android.view.View
import androidx.recyclerview.widget.RecyclerView
import hr.stipanic.tomislav.thrifty.data.model.Message
import hr.stipanic.tomislav.thrifty.databinding.MessageItemIncBinding
import hr.stipanic.tomislav.thrifty.utils.StringUtils

class IncMessageViewHolder(private val messageItemIncBinding: MessageItemIncBinding) :
    RecyclerView.ViewHolder(messageItemIncBinding.root) {

    fun onBind(messageList: List<Message>, position: Int) {
        if(position != 0 && messageList[position - 1].userId == messageList[position].userId) {
            messageItemIncBinding.chatNicknameInc.visibility = View.GONE
        } else {
            messageItemIncBinding.chatNicknameInc.visibility = View.VISIBLE
        }
        messageItemIncBinding.message = messageList[position]
        messageItemIncBinding.stringUtils = StringUtils
        messageItemIncBinding.executePendingBindings()
    }
}