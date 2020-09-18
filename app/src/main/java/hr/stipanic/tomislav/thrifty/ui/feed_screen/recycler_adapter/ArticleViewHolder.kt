package hr.stipanic.tomislav.thrifty.ui.feed_screen.recycler_adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import hr.stipanic.tomislav.thrifty.R
import hr.stipanic.tomislav.thrifty.data.model.Article
import hr.stipanic.tomislav.thrifty.databinding.ArticleItemMaterialBinding
import hr.stipanic.tomislav.thrifty.utils.StringUtils

class ArticleViewHolder(private val articleItemMaterialBinding: ArticleItemMaterialBinding) :
    RecyclerView.ViewHolder(articleItemMaterialBinding.root) {

    init {
        itemView.setOnClickListener {}
    }

    fun onBind(
        articleList: List<Article>,
        position: Int,
        onItemSelected: (String) -> Unit,
        onEditSelected: (String) -> Unit,
        isProfileFragment: Boolean
    ) {
        if (isProfileFragment) {
            articleItemMaterialBinding.editArticleButton.visibility = View.VISIBLE
            articleItemMaterialBinding.editArticleButton.setOnClickListener {
                onEditSelected(articleList[position].id)
            }
        }
        val badgeCounter: TextView =
            articleItemMaterialBinding.root.findViewById(R.id.badge_counter)
        if (articleList[position].messages.isEmpty()) {
            badgeCounter.visibility = View.INVISIBLE
        } else {
            badgeCounter.text = articleList[position].messages.size.toString()
        }
        articleItemMaterialBinding.article = articleList[position]
        articleItemMaterialBinding.stringUtils = StringUtils
        articleItemMaterialBinding.executePendingBindings()
        articleItemMaterialBinding.root.setOnClickListener {
            onItemSelected(articleList[position].id)
        }

    }


    companion object {
        @JvmStatic
        @BindingAdapter("imageUrl")
        fun loadImage(view: ImageView, url: String?) {
            if (!url.isNullOrEmpty()) {
                Glide.with(view.context).load(url).into(view)
            }
        }
    }
}