package com.sevenlearn.nike.data.repo

import com.sevenlearn.nike.data.Comment
import com.sevenlearn.nike.data.repo.source.CommentDataSource
import io.reactivex.Single

class CommentRepositoryImpl(val commentDataSource: CommentDataSource) : CommentRepository {
    override fun getAll(productId: Int): Single<List<Comment>> = commentDataSource.getAll(productId)

    override fun insert(prosuctId: Int, title: String, contect: String): Single<Comment> {
        return commentDataSource.insert(prosuctId,title,contect)
    }
}