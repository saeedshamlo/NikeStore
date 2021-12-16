package com.sevenlearn.nike.data.repo.source

import com.google.gson.JsonObject
import com.sevenlearn.nike.data.TokenResponse
import com.sevenlearn.nike.services.http.ApiService
import com.sevenlearn.nikestore.data.MessageResponse
import io.reactivex.Single

const val CLIENT_ID = 2
const val CLIENT_SECRET = "kyj1c9sVcksqGU4scMX7nLDalkjp2WoqQEf8PKAC"
class UserRemoteDataSource(val apiService: ApiService):UserDateSource {
    override fun login(username: String, password: String): Single<TokenResponse> {
        return apiService.login(JsonObject().apply {
            addProperty("username", username)
            addProperty("password", password)
            addProperty("grant_type", "password")
            addProperty("client_id", CLIENT_ID)
            addProperty("client_secret", CLIENT_SECRET)
        })
    }

    override fun signUp(username: String, password: String): Single<MessageResponse> {

        return apiService.signUp(JsonObject().apply {

            addProperty("email", username)
            addProperty("password", password)
        })
    }
    override fun loadToken() {
        TODO("Not yet implemented")
    }

    override fun saveToken(token: String, refreshToken: String) {
        TODO("Not yet implemented")
    }

    override fun saveUserName(username: String) {
        TODO("Not yet implemented")
    }

    override fun getUserName(): String {
        TODO("Not yet implemented")
    }

    override fun signOut() {
        TODO("Not yet implemented")
    }
}