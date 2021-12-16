package com.sevenlearn.nike.data.repo.source

import com.sevenlearn.nike.data.Comment
import io.reactivex.Single

interface CommentDataSource {

    fun getAll(productId:Int): Single<List<Comment>>

    fun insert(prosuctId:Int,title:String,contect:String): Single<Comment>
}