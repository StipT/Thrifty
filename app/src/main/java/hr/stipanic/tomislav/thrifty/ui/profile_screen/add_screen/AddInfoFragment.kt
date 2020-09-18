package hr.stipanic.tomislav.thrifty.ui.profile_screen.add_screen

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import hr.stipanic.tomislav.thrifty.R
import hr.stipanic.tomislav.thrifty.data.model.Article
import hr.stipanic.tomislav.thrifty.utils.Constants.CATEGORY_EXTRA
import hr.stipanic.tomislav.thrifty.utils.Constants.DESC_EXTRA
import hr.stipanic.tomislav.thrifty.utils.Constants.PRICE_EXTRA
import hr.stipanic.tomislav.thrifty.utils.Constants.TITLE_EXTRA
import hr.stipanic.tomislav.thrifty.utils.RxBindingHelper
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.observers.DisposableObserver
import kotlinx.android.synthetic.main.fragment_add_info.*
import kotlinx.android.synthetic.main.fragment_login.*

class AddInfoFragment : Fragment() {

    private lateinit var navController: NavController
    private lateinit var formObservable: Observable<Boolean>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_info, container, false)
    }

    override fun onResume() {
        super.onResume()
        formValidation()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navController = findNavController()
        addInfoNextButton.setOnClickListener { goToAddLocationFragment() }
        addInfoBackButton.setOnClickListener { navController.popBackStack() }
        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.categories,
            R.layout.spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        addInfoSpinner.adapter = adapter
    }

    private fun isValidForm(title: String, price: String): Boolean? {
        val isTitle = title.isNotEmpty()
        if (!isTitle) {
            addInfoTitleEditBox.error = "Title can not be empty"
        }
        val isPrice = price.isNotEmpty()
        if (!isPrice) {
            addInfoPrice.error = "Title can not be empty"
        }
        return isTitle && isPrice
    }

    private fun formValidation() {
        val titleObservable: Observable<String> =
            RxBindingHelper.getObservableFrom(addInfoTitleEditBox)
        val priceObservable: Observable<String> =
            RxBindingHelper.getObservableFrom(addInfoPrice)
        formObservable = Observable.combineLatest<String, String, Boolean>(
            titleObservable,
            priceObservable,
            BiFunction<String?, String?, Boolean?> { t1, t2 -> isValidForm(t1, t2)!! })
        formObservable.subscribe(object : DisposableObserver<Boolean?>() {
            override fun onNext(aBoolean: Boolean) {
                addInfoNextButton.isEnabled = aBoolean
            }

            override fun onError(e: Throwable) {}
            override fun onComplete() {}
        })
    }

    private fun goToAddLocationFragment() {
        var price = Pair(PRICE_EXTRA, 0f)
        if (addInfoPrice.text.toString().isNotEmpty()) price =
            Pair(PRICE_EXTRA, addInfoPrice.text.toString().toFloat())
        val category = Pair(CATEGORY_EXTRA, addInfoSpinner.selectedItem.toString())
        val title = Pair(TITLE_EXTRA, addInfoTitleEditBox.text.toString())
        val desc = Pair(DESC_EXTRA, addInfoDescription.text.toString())
        val bundle = bundleOf(category, title, desc, price)
        navController.navigate(R.id.action_addInfoFragment_to_addLocationFragment, bundle)
    }
}