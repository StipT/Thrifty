package hr.stipanic.tomislav.thrifty.repository.remote_data_source.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import hr.stipanic.tomislav.thrifty.repository.remote_data_source.auth.AuthSource
import hr.stipanic.tomislav.thrifty.utils.Constants.USERS_COLLECTION
import io.reactivex.Completable
import java.util.*

class AuthSourceImpl(
    private var firebaseAuth: FirebaseAuth,
    private var firebaseFirestore: FirebaseFirestore
) : AuthSource {

    override fun register(email: String, password: String?, name: String): Completable {
        return Completable.create { emitter ->
            firebaseAuth.createUserWithEmailAndPassword(email, password!!)
                .addOnFailureListener { e -> emitter.onError(e) }
                .addOnCompleteListener {
                    val uid = it.result!!.user!!.uid
                    val map = HashMap<String, Any>()
                    map["email"] = email
                    map["nickname"] = name
                    map["userId"] = uid
                    firebaseFirestore.collection(USERS_COLLECTION)
                        .document(uid).set(map)
                        .addOnFailureListener { e -> emitter.onError(e) }
                        .addOnSuccessListener { emitter.onComplete() }
                }
        }
    }

    override fun login(email: String?, password: String?): Completable {
        return Completable.create { emitter ->
            firebaseAuth.signInWithEmailAndPassword(email!!, password!!)
                .addOnFailureListener { e -> emitter.onError(e) }
                .addOnSuccessListener { emitter.onComplete() }
        }
    }

    override fun logout() {
        firebaseAuth.signOut()
    }
}