package hr.stipanic.tomislav.thrifty.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import hr.stipanic.tomislav.thrifty.repository.remote_data_source.auth.AuthSource
import hr.stipanic.tomislav.thrifty.utils.State
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class LoginViewModel(
    private val authSource: AuthSource
) : ViewModel() {

    private val onLogin: MediatorLiveData<State> = MediatorLiveData()
    private val disposable = CompositeDisposable()


    fun login(email: String?, password: String?) {
        authSource.login(email, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {
                    disposable.add(d)
                    onLogin.value = State.LOADING
                }

                override fun onComplete() {
                    onLogin.value = State.SUCCESS
                }

                override fun onError(e: Throwable) {
                    onLogin.value = State.ERROR
                }
            })
    }

    fun observeLogin(): LiveData<State> {
        return onLogin
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}