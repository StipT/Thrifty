package hr.stipanic.tomislav.thrifty.repository.remote_data_source.auth

import io.reactivex.Completable

interface AuthSource {

    fun register(email: String, password: String?, name: String): Completable

    fun login(email: String?, password: String?): Completable

    fun logout()
}