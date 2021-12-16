package com.sevenlearn.nike.feature.profile

import com.sevenlearn.nike.common.NikeViewModel
import com.sevenlearn.nike.data.TokenContainer
import com.sevenlearn.nike.data.repo.UserRepository

class ProfileViewModel(val userRepository: UserRepository) : NikeViewModel() {

    val username: String
        get() = userRepository.getUserName()

    val isSignedIn: Boolean
        get() = TokenContainer.token != null


    fun signOut(){
        userRepository.signOut()
    }
}