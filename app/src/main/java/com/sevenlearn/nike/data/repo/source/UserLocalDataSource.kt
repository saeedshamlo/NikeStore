package com.sevenlearn.nike.data.repo.source

import android.content.SharedPreferences
import com.sevenlearn.nike.data.TokenContainer
import com.sevenlearn.nike.data.TokenResponse
import com.sevenlearn.nikestore.data.MessageResponse
import io.reactivex.Single


class UserLocalDataSource(val sharedPreferences: SharedPreferences) : UserDateSource {
    override fun login(username: String, password: String): Single<TokenResponse> {
        TODO("Not yet implemented")
    }

    override fun signUp(username: String, password: String): Single<MessageResponse> {
        TODO("Not yet implemented")
    }

    override fun loadToken() {
        TokenContainer.update(
            sharedPreferences.getString("access_token", null),
            sharedPreferences.getString("refresh_token", null)
        )
    }

    override fun saveToken(token: String, refreshToken: String) {
        sharedPreferences.edit().apply {
            putString("access_token", token)
            putString("refresh_token", refreshToken)
        }.apply()
    }

    override fun saveUserName(username: String) {
        sharedPreferences.edit().apply {
            putString("username", username)
        }.apply()
    }

    override fun getUserName(): String {
        return sharedPreferences.getString("username", "") ?: ""
    }

    override fun signOut() {
        sharedPreferences.edit().apply {
            clear()
        }.apply()
    }


}