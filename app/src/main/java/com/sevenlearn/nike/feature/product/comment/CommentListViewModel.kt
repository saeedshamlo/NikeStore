package com.sevenlearn.nike.feature.product.comment

import androidx.lifecycle.MutableLiveData
import com.sevenlearn.nike.common.NikeSingleObserver
import com.sevenlearn.nike.common.NikeViewModel
import com.sevenlearn.nike.common.asyncNetworkRequest
import com.sevenlearn.nike.data.Comment
import com.sevenlearn.nike.data.repo.CommentRepository
import io.reactivex.Single

class CommentListViewModel(productId: Int, val commentRepository: CommentRepository) : NikeViewModel() {
    val commentsLiveData = MutableLiveData<List<Comment>>()

    init {
        progressBarLiveData.value = true
        commentRepository.getAll(productId)
            .asyncNetworkRequest()
            .doFinally { progressBarLiveData.value = false }
            .subscribe(object : NikeSingleObserver<List<Comment>>(compositeDisposable) {
                override fun onSuccess(t: List<Comment>) {
                    commentsLiveData.value = t
                }
            })
    }

    fun addComment(prosuctId: Int, title: String, contect: String):Single<Comment>{
        return commentRepository.insert(prosuctId,title,contect)
    }
}