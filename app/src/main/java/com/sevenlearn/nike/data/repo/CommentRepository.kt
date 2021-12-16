package com.sevenlearn.nike.data.repo

import com.sevenlearn.nike.data.Comment
import io.reactivex.Single

interface CommentRepository {

    fun getAll(productId:Int): Single<List<Comment>>

    fun insert(prosuctId:Int,title:String,contect:String): Single<Comment>
}