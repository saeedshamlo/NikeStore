package com.sevenlearn.nike.data.repo.source

import com.google.gson.JsonObject
import com.sevenlearn.nike.data.Comment
import com.sevenlearn.nike.services.http.ApiService
import io.reactivex.Single

class CommentRemoteDataSource(val apiService: ApiService):CommentDataSource {
    override fun getAll(productId:Int): Single<List<Comment>> = apiService.getComments(productId)

    override fun insert(prosuctId: Int, title: String, contect: String): Single<Comment> {
        return apiService.addComment(JsonObject().apply {
            addProperty("title",title)
            addProperty("content",contect)
            addProperty("product_id",prosuctId)
        })
    }
}