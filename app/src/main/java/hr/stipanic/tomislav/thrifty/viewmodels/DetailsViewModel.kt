package hr.stipanic.tomislav.thrifty.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hr.stipanic.tomislav.thrifty.data.model.Article
import hr.stipanic.tomislav.thrifty.data.model.User
import hr.stipanic.tomislav.thrifty.repository.Repository
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class DetailsViewModel(
    private val repository: Repository
) : ViewModel() {

    private val disposable = CompositeDisposable()
    private val user: MutableLiveData<User> = MutableLiveData()
    private val article: MutableLiveData<Article> = MutableLiveData()

    fun getArticle(id: String): LiveData<Article> {
        if (article.value == null) {
            loadArticle(id)
        }
        return article
    }

    fun getUser(userId: String): LiveData<User> {
        if(user.value == null) {
            loadUser(userId)
        }
        return user
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

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}