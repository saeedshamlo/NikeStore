package com.sevenlearn.nike.feature.auth

import com.sevenlearn.nike.common.NikeViewModel
import com.sevenlearn.nike.data.repo.UserRepository
import io.reactivex.Completable

class AuthViewModel(private val userRepository: UserRepository) : NikeViewModel() {

    fun login(email: String, password: String): Completable {
        progressBarLiveData.value = true
        return userRepository.login(email, password).doFinally {
            progressBarLiveData.postValue(false)
        }
    }

    fun signUp(email: String, password: String): Completable {
        progressBarLiveData.value = true
        return userRepository.signUp(email, password).doFinally {
            progressBarLiveData.postValue(false)
        }
    }
}