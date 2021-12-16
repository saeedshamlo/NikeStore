package com.sevenlearn.nike.feature.favorite

import androidx.lifecycle.MutableLiveData
import com.sevenlearn.nike.common.NikeCompletableObserver
import com.sevenlearn.nike.common.NikeSingleObserver
import com.sevenlearn.nike.common.NikeViewModel
import com.sevenlearn.nike.common.asyncNetworkRequest
import com.sevenlearn.nike.data.Product
import com.sevenlearn.nike.data.repo.ProductRepository
import io.reactivex.schedulers.Schedulers

class FavoriteProductViewModel(val productRepository: ProductRepository): NikeViewModel() {


    val productLIveData =MutableLiveData<List<Product>>()
    init {
        productRepository.getFavoriteProducts()
            .asyncNetworkRequest()
            .subscribe(object :NikeSingleObserver<List<Product>>(compositeDisposable){
                override fun onSuccess(t: List<Product>) {
                    productLIveData.value = t
                }
            })
    }
    fun removeFromFavorite(product: Product){
        productRepository.deleteFromFavorites(product)
            .subscribeOn(Schedulers.io())
            .subscribe(object :NikeCompletableObserver(compositeDisposable){
                override fun onComplete() {
                }
            })

    }
}