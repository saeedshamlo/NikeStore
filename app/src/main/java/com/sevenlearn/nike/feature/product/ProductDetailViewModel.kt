package com.sevenlearn.nike.feature.product

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import com.sevenlearn.nike.common.*
import com.sevenlearn.nike.data.Comment
import com.sevenlearn.nike.data.Product
import com.sevenlearn.nike.data.repo.CartRepository
import com.sevenlearn.nike.data.repo.CommentRepository
import com.sevenlearn.nike.data.repo.ProductRepository
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers

class ProductDetailViewModel(bundle: Bundle, commentRepository: CommentRepository,val cartRepository: CartRepository,val productRepository: ProductRepository) :
    NikeViewModel() {
    val productLiveData = MutableLiveData<Product>()
    val commentsLiveData = MutableLiveData<List<Comment>>()

    init {
        productLiveData.value = bundle.getParcelable(EXTRA_KEY_DATA)
        progressBarLiveData.value = true
        commentRepository.getAll(productLiveData.value!!.id)
            .asyncNetworkRequest()
            .doFinally { progressBarLiveData.value = false }
            .subscribe(object : NikeSingleObserver<List<Comment>>(compositeDisposable) {
                override fun onSuccess(t: List<Comment>) {
                    commentsLiveData.value = t
                }
            })
    }
    fun onAddToCartBtn():Completable{
        return  cartRepository.addToCart(productLiveData.value!!.id).ignoreElement()
    }

    fun addProductToFavorite(product: Product) {
        if (product.isFavorite) {
            productRepository.deleteFromFavorites(product)
                .subscribeOn(Schedulers.io())
                .subscribe(object : NikeCompletableObserver(compositeDisposable) {
                    override fun onComplete() {
                        product.isFavorite = false
                    }
                })
        } else {
            productRepository.addToFavorites(product)
                .subscribeOn(Schedulers.io())
                .subscribe(object : NikeCompletableObserver(compositeDisposable) {
                    override fun onComplete() {
                        product.isFavorite = true
                    }
                })
        }
    }
}