package hr.stipanic.tomislav.thrifty.ui.auth_screen

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import hr.stipanic.tomislav.thrifty.R
import hr.stipanic.tomislav.thrifty.databinding.FragmentRegisterBinding
import hr.stipanic.tomislav.thrifty.utils.LoadingDialog
import hr.stipanic.tomislav.thrifty.utils.RxBindingHelper
import hr.stipanic.tomislav.thrifty.utils.State
import hr.stipanic.tomislav.thrifty.utils.showSnackBar
import hr.stipanic.tomislav.thrifty.viewmodels.RegisterViewModel
import io.reactivex.Observable
import io.reactivex.functions.Function4
import io.reactivex.observers.DisposableObserver
import kotlinx.android.synthetic.main.fragment_register.*
import org.koin.android.viewmodel.ext.android.viewModel

class RegisterFragment : Fragment(), View.OnClickListener {
    private val registerViewModel by viewModel<RegisterViewModel>()
    private lateinit var navController: NavController
    private lateinit var formObservable: Observable<Boolean>
    private lateinit var loadingDialog: LoadingDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentRegisterBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        loadingDialog = LoadingDialog()
        subscribeObservers()
        formValidation()
        registerButton.setOnClickListener(this)
        registerBackButton.setOnClickListener(this)
    }

    private fun subscribeObservers() {
        registerViewModel.observeRegister().observe(viewLifecycleOwner,
            Observer<Any?> { stateResource ->
                if (stateResource != null) {
                    when (stateResource) {
                        State.LOADING -> {
                            loadingDialog.show(childFragmentManager, "loadingDialog")
                        }
                        State.SUCCESS -> {
                            showSnackBar("Registration successful")
                            loadingDialog.dismiss()
                            moveToLoginActivity()
                        }
                        State.ERROR -> {
                            loadingDialog.dismiss()
                            showSnackBar("Registration failed! Try again later.")
                        }
                    }
                }
            })
    }

    private fun moveToLoginActivity() =
        navController.navigate(R.id.action_registerFragment_to_loginFragment)

    private fun formValidation() {
        val nicknameObservable: Observable<String> = RxBindingHelper.getObservableFrom(
            registerNickname
        )
        val emailObservable: Observable<String> = RxBindingHelper.getObservableFrom(registerEmail)
        val passwordObservable: Observable<String> = RxBindingHelper.getObservableFrom(
            registerPassword
        )
        val passwordReObservable: Observable<String> = RxBindingHelper.getObservableFrom(
            registerPasswordRe
        )
        formObservable = Observable.combineLatest(
            nicknameObservable,
            emailObservable,
            passwordObservable,
            passwordReObservable,
            Function4<String, String, String, String, Boolean> { t1, t2, t3, t4 ->
                isValidForm(
                    t2,
                    t3,
                    t4,
                    t1
                )
            })

        formObservable.subscribe(object : DisposableObserver<Boolean?>() {
            override fun onNext(aBoolean: Boolean) { registerButton.isEnabled = aBoolean }
            override fun onError(e: Throwable) {}
            override fun onComplete() {}
        })
    }


    private fun isValidForm(
        email: String,
        password: String,
        rePassword: String,
        displayName: String
    ): Boolean {
        val isDisplayName = displayName.isNotEmpty()
        if (!isDisplayName) {
            registerNickname.error = "Please enter valid name"
        }
        val isEmail = Patterns.EMAIL_ADDRESS.matcher(email).matches() && !email.isEmpty()
        if (!isEmail) {
            registerEmail.error = "Please enter valid email"
        }
        val isPassword = password.length > 6 && !password.isEmpty()
        if (!isPassword) {
            registerPassword.error = "Password must be greater then 6 digit"
        }
        val isRePassword = password == rePassword
        if (!isRePassword) {
            registerPasswordRe.error = "Passwords are not the same"
        }
        return isDisplayName && isEmail && isPassword
    }

    private fun register() {
        val nickname = registerNickname.text.toString()
        val email = registerEmail.text.toString()
        val password = registerPassword.text.toString()
        registerViewModel.register(email, password, nickname)
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            registerButton.id -> register()
            registerBackButton.id -> navController.popBackStack()
        }
    }
}