package hr.stipanic.tomislav.thrifty.viewmodels

import hr.stipanic.tomislav.thrifty.data.model.Message

import androidx.lifecycle.*
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
import java.util.*
import kotlin.collections.ArrayList

class ChatViewModel(
    private val repository: Repository
) : ViewModel() {

    private val disposable = CompositeDisposable()
    private val onQuery: MediatorLiveData<State> = MediatorLiveData()
    private val onSend: MediatorLiveData<State> = MediatorLiveData()
    private val messageList: MutableLiveData<List<Message>> = MutableLiveData()
    private val article: MutableLiveData<Article> = MutableLiveData()
    private val currentUser: MutableLiveData<User> = MutableLiveData()

    fun getMessages(id: String): LiveData<List<Message>> {
        if (messageList.value == null) {
            loadMessages(id)
        }
        return messageList
    }

    fun getCurrentUser(): LiveData<User> {
        if (currentUser.value == null) {
            loadCurrentUser()
        }
        return currentUser
    }

    private fun loadCurrentUser() {
        repository.getCurrentUser()
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.toObservable()
            ?.subscribe(object : Observer<User> {
                override fun onSubscribe(d: Disposable) {
                    disposable.add(d)
                    onQuery.value = State.LOADING
                }

                override fun onNext(t: User) {
                    currentUser.value = t
                }

                override fun onError(e: Throwable) {
                    onQuery.value = State.ERROR
                }

                override fun onComplete() {
                    onQuery.value = State.SUCCESS
                }
            })
    }

    private fun loadMessages(id: String) {
        repository.getSingleArticle(id)!!
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .toObservable()
            .subscribe(object : Observer<Article> {
                override fun onSubscribe(d: Disposable) {
                    disposable.add(d)
                    onQuery.value = State.LOADING
                }

                override fun onNext(t: Article) {
                    article.value = t
                    messageList.value = t.messages
                }

                override fun onError(e: Throwable) {
                    onQuery.value = State.ERROR
                }

                override fun onComplete() {
                    onQuery.value = State.SUCCESS
                }
            })
    }

    fun sendMessage(message: String) {
        repository.getCurrentUser()
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.toObservable()
            ?.subscribe(object : Observer<User> {
                override fun onSubscribe(d: Disposable) {
                    disposable.add(d)
                    onSend.value = State.LOADING
                }

                override fun onNext(t: User) {
                    val newMessage = Message(t.userId, t.nickname, message, Date().time)
                    val messageList: List<Message>  = messageList.value!!
                    (messageList as ArrayList).add(newMessage)
                    article.value?.messages = messageList
                    repository.updateArticle(article.value!!)
                        ?.subscribeOn(Schedulers.io())
                        ?.observeOn(AndroidSchedulers.mainThread())
                        ?.subscribe(object : CompletableObserver {
                            override fun onSubscribe(d: Disposable) {
                                disposable.add(d)
                            }

                            override fun onComplete() {
                                onSend.value = State.SUCCESS
                            }

                            override fun onError(e: Throwable) {
                                onSend.value = State.ERROR
                            }
                        })
                }

                override fun onError(e: Throwable) {
                    onSend.value = State.ERROR
                }

                override fun onComplete() {
                    onSend.value = State.SUCCESS
                }
            })
    }

    fun observeQuery(): LiveData<State> {
        return onQuery
    }

    fun observeSend(): LiveData<State> {
        return onSend
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}