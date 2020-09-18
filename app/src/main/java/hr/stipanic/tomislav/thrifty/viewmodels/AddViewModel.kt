package hr.stipanic.tomislav.thrifty.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hr.stipanic.tomislav.thrifty.data.model.Article
import hr.stipanic.tomislav.thrifty.repository.Repository
import hr.stipanic.tomislav.thrifty.utils.State
import io.reactivex.CompletableObserver
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class AddViewModel(
    private val repository: Repository
) : ViewModel() {

    private val onInsert: MediatorLiveData<State> = MediatorLiveData()
    private val image: MutableLiveData<ByteArray> = MutableLiveData()
    private val imageUri: MutableLiveData<String> = MutableLiveData()
    private val disposable = CompositeDisposable()
    private val articleState: MutableLiveData<Article> = MutableLiveData()


    fun setImage(imageBytes: ByteArray) {
        image.value = imageBytes
    }

    fun uploadArticle(newArticle: Article) {
        repository.uploadImage(image.value!!)!!
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .toObservable()
            .subscribe(object : Observer<String> {
                override fun onSubscribe(d: Disposable) {
                    disposable.add(d)
                    onInsert.value = State.LOADING
                }

                override fun onNext(t: String) {
                    imageUri.value = t
                    newArticle.imgSrc = imageUri.value!!
                    articleState.value = newArticle
                }

                override fun onError(e: Throwable) {
                    onInsert.value = State.ERROR
                }

                override fun onComplete() {
                    repository.insertArticle(articleState.value!!)!!
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object : CompletableObserver {
                            override fun onSubscribe(d: Disposable) {
                                disposable.add(d)

                            }

                            override fun onComplete() {
                                onInsert.value = State.SUCCESS
                            }

                            override fun onError(e: Throwable) {
                                onInsert.value = State.ERROR
                            }
                        })
                }
            })
    }

    fun observeInsert(): LiveData<State> {
        return onInsert
    }


    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}