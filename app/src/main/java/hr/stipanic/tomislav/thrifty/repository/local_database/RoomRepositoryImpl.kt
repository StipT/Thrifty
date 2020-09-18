package hr.stipanic.tomislav.thrifty.repository.local_database

import hr.stipanic.tomislav.thrifty.data.model.Article
import hr.stipanic.tomislav.thrifty.data.model.User
import hr.stipanic.tomislav.thrifty.repository.local_database.dao.ArticleDao
import hr.stipanic.tomislav.thrifty.repository.local_database.dao.UserDao


class RoomRepositoryImpl(db: ThriftyDatabase) : RoomRepository {

    private val articleDao: ArticleDao = db.articleDao()
    private val userDao: UserDao = db.userDao()

    override fun addNewArticle(article: Article) = articleDao.addNewArticle(article)

    override fun addNewUser(user: User) = userDao.addNewUser(user)

    override fun getSingleArticle(id: String) = articleDao.getSingleArticle(id)

    override fun deleteArticle(id: String) = articleDao.deleteArticle(id)

    override fun getUsersArticles(userId: String): List<Article> = articleDao.getUsersArticles(userId)

    override fun getArticlesByCategory(category: String) = articleDao.getArticlesByCategory(category)

}