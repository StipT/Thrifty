package hr.stipanic.tomislav.thrifty.repository

import com.google.firebase.firestore.QuerySnapshot
import hr.stipanic.tomislav.thrifty.data.model.Article
import hr.stipanic.tomislav.thrifty.data.model.Message
import hr.stipanic.tomislav.thrifty.data.model.User
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

interface Repository {

    fun insertArticle(task: Article): Completable?

    fun getArticleList(category: String): Single<List<Article>>?

    fun getArticleListByProfile(): Single<List<Article>>?

    fun getSingleArticle(id: String): Single<Article>?

    fun getUser(userId: String): Single<User>?

    fun getCurrentUser(): Single<User>?

    fun uploadImage(imageBytes: ByteArray): Single<String>?

    fun updateArticle(task: Article): Completable?

    fun deleteArticle(id: String): Completable?
}