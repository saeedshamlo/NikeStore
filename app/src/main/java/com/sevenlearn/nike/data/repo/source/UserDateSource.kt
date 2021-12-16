package com.sevenlearn.nike.data.repo.source

import com.sevenlearn.nike.data.TokenResponse
import com.sevenlearn.nikestore.data.MessageResponse
import io.reactivex.Completable
import io.reactivex.Single

interface UserDateSource {

    fun login(username:String,password:String):Single<TokenResponse>

    fun signUp(username:String,password:String):Single<MessageResponse>

    fun loadToken()

    fun saveToken(token:String,refreshToken:String)

    fun saveUserName(username:String)
    fun getUserName():String
    fun signOut()

}