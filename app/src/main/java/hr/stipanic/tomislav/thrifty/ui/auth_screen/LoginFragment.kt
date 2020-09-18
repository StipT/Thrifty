package hr.stipanic.tomislav.thrifty.ui.auth_screen

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import hr.stipanic.tomislav.thrifty.R
import hr.stipanic.tomislav.thrifty.databinding.FragmentLoginBinding
import hr.stipanic.tomislav.thrifty.utils.Constants.IS_LOGGED_IN
import hr.stipanic.tomislav.thrifty.utils.Constants.LOGIN
import hr.stipanic.tomislav.thrifty.utils.LoadingDialog
import hr.stipanic.tomislav.thrifty.utils.RxBindingHelper
import hr.stipanic.tomislav.thrifty.utils.State
import hr.stipanic.tomislav.thrifty.utils.showSnackBar
import hr.stipanic.tomislav.thrifty.viewmodels.LoginViewModel
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.observers.DisposableObserver
import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.android.viewmodel.ext.android.viewModel

class LoginFragment : Fragment(), LifecycleOwner, View.OnClickListener {
    private val loginViewModel by viewModel<LoginViewModel>()
    private lateinit var navController: NavController
    private lateinit var formObservable: Observable<Boolean>
    private lateinit var loadingDialog: LoadingDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentLoginBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        loadingDialog = LoadingDialog()
        subscribeObservers()

        loginButton.setOnClickListener(this)
        goToRegisterButton.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        formValidation()
    }

    private fun subscribeObservers() {
        loginViewModel.observeLogin().observe(viewLifecycleOwner,
            Observer<Any?> {
                if (it != null) {
                    when (it) {
                        State.LOADING -> loadingDialog.show(
                            childFragmentManager,
                            "loadingDialog"
                        )
                        State.SUCCESS -> {
                            loadingDialog.dismiss()
                            showSnackBar("Login Successful!")
                            moveToHomeFragment()
                        }
                        State.ERROR -> {
                            loadingDialog.dismiss()
                            showSnackBar("Login Failed!")
                        }
                    }
                }
            })
    }

    private fun formValidation() {
        val emailObservable: Observable<String> = RxBindingHelper.getObservableFrom(emailLogin)
        val passwordObservable: Observable<String> =
            RxBindingHelper.getObservableFrom(passwordLogin)
        formObservable = Observable.combineLatest<String, String, Boolean>(
            emailObservable,
            passwordObservable,
            BiFunction<String?, String?, Boolean?> { t1, t2 -> isValidForm(t1, t2)!! })
        formObservable.subscribe(object : DisposableObserver<Boolean?>() {
            override fun onNext(aBoolean: Boolean) { loginButton.isEnabled = aBoolean }
            override fun onError(e: Throwable) {}
            override fun onComplete() {}
        })
    }

    private fun performLogin() {
        val email: String = emailLogin.text.toString().trim { it <= ' ' }
        val password: String = passwordLogin.text.toString().trim { it <= ' ' }
        loginViewModel.login(email, password)
    }


    private fun isValidForm(email: String, password: String): Boolean? {
        val isEmail = Patterns.EMAIL_ADDRESS.matcher(email).matches() && email.isNotEmpty()
        if (!isEmail) {
            emailLogin.error = "Please enter valid email"
        }
        val isPassword = password.length > 6 && password.isNotEmpty()
        if (!isPassword) {
            passwordLogin.error = "Password must be greater then 6 digit"
        }
        return isEmail && isPassword
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            loginButton.id -> performLogin()
            goToRegisterButton.id -> moveToRegisterFragment()
        }
    }

    private fun moveToHomeFragment() {
        requireContext().getSharedPreferences(LOGIN, 0).edit().putBoolean(IS_LOGGED_IN, true)
            .apply()
        navController.navigate(R.id.action_loginFragment_to_categoryFragment)
    }


    private fun moveToRegisterFragment() =
        navController.navigate(R.id.action_loginFragment_to_registerFragment)
}


