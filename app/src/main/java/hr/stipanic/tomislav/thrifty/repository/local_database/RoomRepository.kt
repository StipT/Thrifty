package hr.stipanic.tomislav.thrifty.repository.local_database


import hr.stipanic.tomislav.thrifty.data.model.Article
import hr.stipanic.tomislav.thrifty.data.model.User

interface RoomRepository {

    fun addNewArticle (article: Article)

    fun addNewUser(user: User)

    fun getSingleArticle(id: String): Article

    fun getUsersArticles(userId: String): List<Article>

    fun getArticlesByCategory(category: String): List<Article>

    fun deleteArticle(id: String)
}