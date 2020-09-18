package hr.stipanic.tomislav.thrifty.ui.chat_screen.recycler_adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import hr.stipanic.tomislav.thrifty.R
import hr.stipanic.tomislav.thrifty.data.model.Message
import hr.stipanic.tomislav.thrifty.databinding.MessageItemIncBinding
import hr.stipanic.tomislav.thrifty.databinding.MessageItemOutBinding
import hr.stipanic.tomislav.thrifty.utils.Constants
import hr.stipanic.tomislav.thrifty.utils.Constants.TYPE_INC_MESSAGE
import hr.stipanic.tomislav.thrifty.utils.Constants.TYPE_OUT_MESSAGE


class MessageRecyclerAdapter() :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var messageList: MutableList<Message> = mutableListOf()
    private var userId: String = "-1"

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(p0.context)
        if (p1 == TYPE_INC_MESSAGE) {
            val binding: MessageItemIncBinding =
                DataBindingUtil.inflate(layoutInflater, R.layout.message_item_inc, p0, false)
            return IncMessageViewHolder(binding)
        } else {
            val binding: MessageItemOutBinding =
                DataBindingUtil.inflate(layoutInflater, R.layout.message_item_out, p0, false)
            return OutMessageViewHolder(binding)
        }
    }

    override fun getItemCount() = messageList.size

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
        if (getItemViewType(p1) == TYPE_OUT_MESSAGE) {
            (p0 as OutMessageViewHolder).onBind(messageList, p1)
        } else {
            (p0 as IncMessageViewHolder).onBind(messageList, p1)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (messageList[position].userId == userId) TYPE_OUT_MESSAGE
        else TYPE_INC_MESSAGE
    }

    fun setUserId(userId: String) {
        this.userId = userId

    }

    fun addItems(list: List<Message>) {
        messageList.clear()
        messageList = list.toMutableList()
        notifyDataSetChanged()
    }
}