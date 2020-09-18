package hr.stipanic.tomislav.thrifty.viewmodels

import androidx.lifecycle.*
import hr.stipanic.tomislav.thrifty.data.model.Article
import hr.stipanic.tomislav.thrifty.repository.Repository
import hr.stipanic.tomislav.thrifty.utils.State
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class FeedListViewModel(
    private val repository: Repository
) : ViewModel() {

    private val disposable = CompositeDisposable()
    private val onQuery: MediatorLiveData<State> = MediatorLiveData()
    private val articleList: MutableLiveData<List<Article>> = MutableLiveData()

    fun getArticles(category: String): LiveData<List<Article>> {
        if (articleList.value == null) {
            loadArticles(category)
        }
        return articleList
    }

    private fun loadArticles(category: String) {
        repository.getArticleList(category)!!
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .toObservable()
            .subscribe(object : Observer<List<Article>> {
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

    fun observeQuery(): LiveData<State> {
        return onQuery
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}