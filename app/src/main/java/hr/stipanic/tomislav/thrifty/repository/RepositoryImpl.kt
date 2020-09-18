package hr.stipanic.tomislav.thrifty.repository

import hr.stipanic.tomislav.thrifty.data.model.Article
import hr.stipanic.tomislav.thrifty.data.model.Message
import hr.stipanic.tomislav.thrifty.data.model.User
import hr.stipanic.tomislav.thrifty.repository.local_database.RoomRepository
import hr.stipanic.tomislav.thrifty.repository.remote_data_source.database.FirestoreSource
import io.reactivex.Completable
import io.reactivex.Single

class RepositoryImpl(
    private val firestoreSource: FirestoreSource,
    private val roomRepository: RoomRepository
) : Repository {

    override fun insertArticle(article: Article): Completable? {
        return firestoreSource.uploadArticle(article)
    }


    override fun getArticleList(category: String): Single<List<Article>>? =
        firestoreSource.getArticleList(category)


    override fun getArticleListByProfile(): Single<List<Article>>? =
        firestoreSource.getArticleListByProfile()


    override fun getSingleArticle(id: String): Single<Article>? {
        return firestoreSource.getSingleArticle(id)
    }

    override fun getCurrentUser(): Single<User>? = firestoreSource.getCurrentUser()


    override fun uploadImage(imageBytes: ByteArray): Single<String>? =
        firestoreSource.uploadImage(imageBytes)


    override fun updateArticle(article: Article): Completable? {
        return firestoreSource.updateArticle(article)
    }

    override fun deleteArticle(id: String): Completable? {
        return firestoreSource.deleteArticle(id)
    }

    override fun getUser(userId: String): Single<User>? {
        return firestoreSource.getUser(userId)
    }
}