package hr.stipanic.tomislav.thrifty.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hr.stipanic.tomislav.thrifty.data.model.Article
import hr.stipanic.tomislav.thrifty.data.model.User
import hr.stipanic.tomislav.thrifty.repository.Repository
import hr.stipanic.tomislav.thrifty.utils.State
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.Observer

class ProfileViewModel(
    private val repository: Repository
) : ViewModel() {

    private val disposable = CompositeDisposable()
    private val articleList: MutableLiveData<List<Article>> = MutableLiveData()
    private val user: MutableLiveData<User> = MutableLiveData()
    private val onQuery: MediatorLiveData<State> = MediatorLiveData()

    fun getArticles(): LiveData<List<Article>> {
        if (articleList.value == null) {
            loadArticles()
        }
        return articleList
    }

    fun getUser(): LiveData<User> {
        if (user.value == null) {
            loadUser()
        }
        return user
    }

    private fun loadArticles() {
        repository.getArticleListByProfile()
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.toObservable()
            ?.subscribe(object : Observer<List<Article>> {
                override fun onSubscribe(d: Disposable) {
                    disposable.add(d)
                    onQuery.value = State.LOADING
                }

                override fun onNext(t: List<Article>) {
                    articleList.value = t
                }

                override fun onError(e: Throwable) {
                    onQuery.value = State.ERROR
                }

                override fun onComplete() {
                    onQuery.value = State.SUCCESS
                }
            })
    }

    private fun loadUser() {
        repository.getCurrentUser()
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

    fun observeQuery(): LiveData<State> {
        return onQuery
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}