package hr.stipanic.tomislav.thrifty.utils

import android.widget.EditText
import com.jakewharton.rxbinding3.widget.textChanges
import io.reactivex.Observable

object RxBindingHelper {
    fun getObservableFrom(editText: EditText): Observable<String> {
        return editText.textChanges().skip(1).map { charSequence -> charSequence.toString() }
    }
}