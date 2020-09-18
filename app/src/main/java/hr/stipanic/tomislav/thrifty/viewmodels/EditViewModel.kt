package hr.stipanic.tomislav.thrifty.viewmodels


import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hr.stipanic.tomislav.thrifty.data.model.Article
import hr.stipanic.tomislav.thrifty.data.model.User
import hr.stipanic.tomislav.thrifty.repository.Repository
import hr.stipanic.tomislav.thrifty.utils.State
import io.reactivex.CompletableObserver
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class EditViewModel(
    private val repository: Repository
) : ViewModel() {

    private val disposable = CompositeDisposable()
    private val user: MutableLiveData<User> = MutableLiveData()
    private val article: MutableLiveData<Article> = MutableLiveData()
    private val imageUri: MutableLiveData<String> = MutableLiveData()
    private val image: MutableLiveData<ByteArray> = MutableLiveData()
    private val onDelete: MediatorLiveData<State> = MediatorLiveData()
    private val onUpdate: MediatorLiveData<State> = MediatorLiveData()


    fun getArticle(id: String): LiveData<Article> {
        if (article.value == null) {
            loadArticle(id)
        }
        return article
    }

    fun getUser(userId: String): LiveData<User> {
        if (user.value == null) {
            loadUser(userId)
        }
        return user
    }

    fun setImage(imageBytes: ByteArray) {
        image.value = imageBytes
    }

    private fun loadArticle(id: String) {
        repository.getSingleArticle(id)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.toObservable()
            ?.subscribe(object : Observer<Article> {
                override fun onSubscribe(d: Disposable) {
                    disposable.add(d)
                }

                override fun onNext(t: Article) {
                    article.value = t
                    loadUser(t.userId)
                }

                override fun onError(e: Throwable) {
                }

                override fun onComplete() {

                }
            })
    }

    private fun loadUser(userId: String) {
        repository.getUser(userId)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.toObservable()
            ?.subscribe(object : Observer<User> {
                override fun onSubscribe(d: Disposable) {
                    disposable.add(d)
                }

                override fun onNext(t: User) {
                    user.value = t
                }

                override fun onError(e: Throwable) {
                }

                override fun onComplete() {
                }
            })
    }

    fun updateArticle(title: String, price: Float, desc: String, isPhotoSelected: Boolean) {
        if(isPhotoSelected.not() && image.value == null) {
            updateValues(title, price, desc)
        } else {
        repository.uploadImage(image.value!!)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.toObservable()
            ?.subscribe(object : Observer<String> {
                override fun onSubscribe(d: Disposable) {
                    disposable.add(d)
                    onUpdate.value = State.LOADING
                }

                override fun onNext(t: String) {
                    imageUri.value = t
                    article.value?.imgSrc = t
                }

                override fun onError(e: Throwable) {
                    onUpdate.value = State.ERROR
                }

                override fun onComplete() {
                    updateValues(title, price, desc)
                }
            })
        }
    }

    private fun updateValues(title: String, price: Float, desc: String) {
        val newArticle = article.value
        newArticle?.title = title
        newArticle?.price = price
        newArticle?.desc = desc
        repository.insertArticle(newArticle!!)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {
                    disposable.add(d)
                    onUpdate.value = State.LOADING
                }

                override fun onComplete() {
                    onUpdate.value = State.SUCCESS
                }

                override fun onError(e: Throwable) {
                    onUpdate.value = State.ERROR
                }
            })
    }

    fun deleteArticle(articleId: String) {
        repository.deleteArticle(articleId)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {
                    disposable.add(d)
                    onDelete.value = State.LOADING
                }

                override fun onComplete() {
                    onDelete.value = State.SUCCESS
                }

                override fun onError(e: Throwable) {
                    onDelete.value = State.ERROR
                }
            })
    }

    fun observeDelete(): LiveData<State> {
        return onDelete
    }


    fun observeUpdate(): LiveData<State> {
        return onUpdate
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}