package com.sevenlearn.nike.data.repo

import com.sevenlearn.nike.data.TokenContainer
import com.sevenlearn.nike.data.TokenResponse
import com.sevenlearn.nike.data.repo.source.UserDateSource
import com.sevenlearn.nike.data.repo.source.UserLocalDataSource
import com.sevenlearn.nike.data.repo.source.UserRemoteDataSource
import io.reactivex.Completable

class UserRpositoryImpl(
    val userLocalDataSource: UserDateSource,
    val userRemoteDataSource: UserDateSource
) : UserRepository {

    override fun login(username: String, password: String): Completable {
        return userRemoteDataSource.login(username, password).doOnSuccess {
            onSuccessfulLogin(username,it)
        }.ignoreElement()
    }

    override fun signUp(username: String, password: String): Completable {
        return userRemoteDataSource.signUp(username, password).flatMap {
            userRemoteDataSource.login(username, password)
        }.doOnSuccess {
            onSuccessfulLogin(username,it)
        }.ignoreElement()
    }

    override fun loadToken() {
        userLocalDataSource.loadToken()
    }

    override fun getUserName(): String {
        return userLocalDataSource.getUserName()
    }

    override fun signOut() {
        userLocalDataSource.signOut()
        TokenContainer.update(null,null)
    }


    private fun onSuccessfulLogin(username:String, tokenResponse: TokenResponse) {
        TokenContainer.update(tokenResponse.access_token, tokenResponse.refresh_token)
        userLocalDataSource.saveToken(tokenResponse.access_token, tokenResponse.refresh_token)
        userLocalDataSource.saveUserName(username)
    }
}