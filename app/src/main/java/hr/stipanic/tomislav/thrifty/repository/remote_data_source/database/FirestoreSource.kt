package hr.stipanic.tomislav.thrifty.repository.remote_data_source.database

import hr.stipanic.tomislav.thrifty.data.model.Article
import hr.stipanic.tomislav.thrifty.data.model.Message
import hr.stipanic.tomislav.thrifty.data.model.User
import io.reactivex.Completable
import io.reactivex.Single

interface FirestoreSource {

    fun getUserUUID(): String

    fun getArticleList(category: String): Single<List<Article>>?

    fun getArticleListByProfile(): Single<List<Article>>?

    fun getSingleArticle(id: String): Single<Article>?

    fun getCurrentUser(): Single<User>?

    fun getUser(userId: String): Single<User>?

    fun uploadImage(image: ByteArray): Single<String>?

    fun uploadArticle(article: Article): Completable?

    fun deleteArticle(id: String): Completable?

    fun updateArticle(article: Article): Completable?
}