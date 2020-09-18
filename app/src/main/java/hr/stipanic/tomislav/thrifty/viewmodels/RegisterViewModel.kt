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


class RegisterViewModel(
    private val authSource: AuthSource
) : ViewModel() {

    private val onRegister: MediatorLiveData<State> = MediatorLiveData()
    private val disposable = CompositeDisposable()


    fun register(email: String, password: String, name: String) {
        authSource.register(email, password, name)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {

                override fun onSubscribe(d: Disposable) {
                    disposable.add(d)
                    println("---------> " + "onSubscribe")
                    onRegister.value = State.LOADING
                }

                override fun onComplete() {
                    onRegister.value = State.SUCCESS
                    println("---------> " + "onComplete")
                }

                override fun onError(e: Throwable) {
                    onRegister.value = State.ERROR
                    println("---------> " + "onError")
                }
            })
    }

    fun observeRegister(): LiveData<State> {
        return onRegister
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}