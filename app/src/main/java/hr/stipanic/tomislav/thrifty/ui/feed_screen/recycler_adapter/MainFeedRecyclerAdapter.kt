package hr.stipanic.tomislav.thrifty.ui.feed_screen.recycler_adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import hr.stipanic.tomislav.thrifty.R
import hr.stipanic.tomislav.thrifty.data.model.Article
import hr.stipanic.tomislav.thrifty.databinding.ArticleItemMaterialBinding
import java.util.*
import kotlin.collections.ArrayList

class MainFeedRecyclerAdapter(
    private val onItemSelected: (String) -> Unit,
    private val onEditSelected: (String) -> Unit,
    private val isProfileFragment: Boolean = false
) :
    RecyclerView.Adapter<ArticleViewHolder>(), Filterable {

    private var articleList: MutableList<Article> = mutableListOf()
    private var articleListAll: MutableList<Article> = mutableListOf()

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ArticleViewHolder {
        val layoutInflater = LayoutInflater.from(p0.context)
        val binding: ArticleItemMaterialBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.article_item_material, p0, false)
        return ArticleViewHolder(binding)
    }

    override fun getItemCount() = articleList.size

    override fun onBindViewHolder(p0: ArticleViewHolder, p1: Int) {
        p0.onBind(articleList, p1, onItemSelected, onEditSelected, isProfileFragment)
    }

    fun addItems(list: List<Article>) {
        articleList.clear()
        articleList = list.toMutableList()
        articleListAll = ArrayList(articleList)
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter? {
        return exampleFilter
    }

    private val exampleFilter: Filter = object : Filter() {

        override fun performFiltering(constraint: CharSequence?): FilterResults? {

            println("I AM HERE -------->")
            val filteredList: MutableList<Article> = ArrayList()
            if (constraint == null || constraint.isEmpty()) {
                filteredList.addAll(articleListAll)
            } else {
                val filterPattern =
                    constraint.toString().toLowerCase().trim()
                for (item in articleListAll) {
                    println("---------------------> " + constraint + " ---------------> " + item.title)
                    if (item.title.toLowerCase().contains(filterPattern.toLowerCase())) {
                        filteredList.add(item)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults) {
            articleList.clear()
            articleList.addAll(results.values as Collection<Article>)
            notifyDataSetChanged()
        }
    }
}