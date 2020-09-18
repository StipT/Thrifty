package hr.stipanic.tomislav.thrifty.repository.local_database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import hr.stipanic.tomislav.thrifty.data.model.Article
import hr.stipanic.tomislav.thrifty.data.model.Message

@Dao
interface ArticleDao {

    @Insert(onConflict = REPLACE)
    fun addNewArticle(article: Article)

    @Query("SELECT * FROM Article WHERE id = :id")
    fun getSingleArticle(id: String): Article

    @Query("SELECT * FROM Article WHERE userId = :userId ORDER BY date DESC")
    fun getUsersArticles(userId: String): List<Article>

    @Query("SELECT * FROM Article WHERE category = :category ORDER BY date DESC")
    fun getArticlesByCategory(category: String): List<Article>

    @Query("DELETE FROM Article WHERE id = :id")
    fun deleteArticle(id: String)
}