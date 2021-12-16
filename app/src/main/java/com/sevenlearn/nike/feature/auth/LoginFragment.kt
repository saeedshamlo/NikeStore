package com.sevenlearn.nike.feature.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.analytics.FirebaseAnalytics
import com.sevenlearn.nike.R
import com.sevenlearn.nike.common.NikeCompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.android.viewmodel.ext.android.viewModel

class LoginFragment : Fragment() {

    val viewModel: AuthViewModel by viewModel()
    val compositeDisposable = CompositeDisposable()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        login_to_sign_up.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)

        }
        loginBtn.setOnClickListener {
            viewModel.login(emailEt.text.toString(), passwordEt.text.toString())
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : NikeCompletableObserver(compositeDisposable) {
                    override fun onComplete() {
                        requireActivity().finish()

                        val bundle =Bundle()
                        bundle.putString(FirebaseAnalytics.Param.METHOD,"app")
                        FirebaseAnalytics.getInstance(requireContext())
                            .logEvent(FirebaseAnalytics.Event.LOGIN,bundle)
                    }

                })
        }



    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }
}