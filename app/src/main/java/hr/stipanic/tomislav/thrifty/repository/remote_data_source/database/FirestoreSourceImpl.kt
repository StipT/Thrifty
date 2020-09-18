package hr.stipanic.tomislav.thrifty.repository.remote_data_source.database

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import hr.stipanic.tomislav.thrifty.data.model.Article
import hr.stipanic.tomislav.thrifty.data.model.Message
import hr.stipanic.tomislav.thrifty.data.model.User
import hr.stipanic.tomislav.thrifty.utils.Constants.ARTICLES_COLLECTION
import hr.stipanic.tomislav.thrifty.utils.Constants.CATEGORY
import hr.stipanic.tomislav.thrifty.utils.Constants.DATE
import hr.stipanic.tomislav.thrifty.utils.Constants.USERS_COLLECTION
import hr.stipanic.tomislav.thrifty.utils.Constants.USER_ID
import io.reactivex.Completable
import io.reactivex.Single
import java.util.*


class FirestoreSourceImpl(
    private val firebaseFirestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
    private val firebaseStorage: FirebaseStorage
) : FirestoreSource {

    override fun getUserUUID(): String = firebaseAuth.currentUser!!.uid


    override fun getArticleList(category: String): Single<List<Article>>? {
        return Single.create {
            firebaseFirestore
                .collection(ARTICLES_COLLECTION)
                .whereEqualTo(CATEGORY, category)
                .orderBy(DATE, Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful && task.result != null) {
                        val articleList: List<Article> =
                            task.result!!.toObjects(Article::class.java)
                        it.onSuccess(articleList)
                    }
                }
        }
    }

    override fun getArticleListByProfile(): Single<List<Article>>? {
        return Single.create {
            firebaseFirestore
                .collection(ARTICLES_COLLECTION)
                .whereEqualTo(USER_ID, getUserUUID())
                .orderBy(DATE, Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful && task.result != null) {
                        val articleList: List<Article> =
                            task.result!!.toObjects(Article::class.java)
                        it.onSuccess(articleList)
                    }
                }
        }
    }

    override fun getSingleArticle(id: String): Single<Article>? {
        return Single.create {
            firebaseFirestore
                .collection(ARTICLES_COLLECTION)
                .document(id)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful && task.result != null) {
                        println(task.result.toString())
                        val article: Article = task.result?.toObject(Article::class.java) as Article
                        it.onSuccess(article)
                    }
                }
        }
    }

    override fun getCurrentUser(): Single<User>? {
        return Single.create {
            firebaseFirestore
                .collection(USERS_COLLECTION)
                .document(getUserUUID())
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful && task.result != null) {
                        val user: User = task.result?.toObject(User::class.java) as User
                        it.onSuccess(user)
                    }
                }
        }
    }

    override fun getUser(userId: String): Single<User>? {
        return Single.create {
            firebaseFirestore
                .collection(USERS_COLLECTION)
                .document(userId)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful && task.result != null) {
                        val user: User = task.result?.toObject(User::class.java) as User
                        it.onSuccess(user)
                    }
                }
        }
    }

    override fun uploadImage(image: ByteArray): Single<String>? {
        return Single.create { e ->
            val storageReference: StorageReference =
                firebaseStorage.reference
                    .child(
                        "images/" + getUserUUID().toString() + "/" + UUID.randomUUID().toString()
                    )
            storageReference
                .putBytes(image)
                .addOnSuccessListener {
                    storageReference.downloadUrl
                        .addOnSuccessListener { uri -> e.onSuccess(uri.toString()) }
                        .addOnFailureListener { error -> e.onError(error) }

                }
        }
    }

    override fun uploadArticle(article: Article): Completable? {
        return Completable.create { e ->
            article.userId = getUserUUID()
            article.id = "${article.userId}${article.date}"
            firebaseFirestore
                .collection(ARTICLES_COLLECTION)
                .document(article.id)
                .set(article)
                .addOnCompleteListener { e.onComplete() }
                .addOnFailureListener { error -> e.onError(error) }
        }
    }

    override fun updateArticle(article: Article): Completable? {
        return Completable.create { e ->
            article.id = "${article.userId}${article.date}"
            firebaseFirestore
                .collection(ARTICLES_COLLECTION)
                .document(article.id)
                .set(article)
                .addOnCompleteListener { e.onComplete() }
                .addOnFailureListener { error -> e.onError(error) }
        }
    }

    override fun deleteArticle(id: String): Completable? {
        return Completable.create { e ->
            firebaseFirestore
                .collection(ARTICLES_COLLECTION)
                .document(id)
                .delete()
                .addOnCompleteListener { e.onComplete() }
                .addOnFailureListener { error -> e.onError(error) }
        }
    }
}