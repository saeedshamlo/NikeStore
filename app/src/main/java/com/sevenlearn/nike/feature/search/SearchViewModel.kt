package com.sevenlearn.nike.feature.search

import com.sevenlearn.nike.common.NikeCompletableObserver
import com.sevenlearn.nike.common.NikeViewModel
import com.sevenlearn.nike.data.Product
import com.sevenlearn.nike.data.repo.ProductRepository
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class SearchViewModel(val productRepository: ProductRepository): NikeViewModel() {

    fun search(query:String):Single<List<Product>>{
        progressBarLiveData.value =true
        return productRepository.searchProduct(query).doFinally { progressBarLiveData.postValue( false)}
    }

    fun addProductToFavorite(product: Product){
        if(product.isFavorite){
            productRepository.deleteFromFavorites(product)
                .subscribeOn(Schedulers.io())
                .subscribe(object : NikeCompletableObserver(compositeDisposable){
                    override fun onComplete() {
                        product.isFavorite =false
                    }
                })
        }
        else{
            productRepository.addToFavorites(product)
                .subscribeOn(Schedulers.io())
                .subscribe(object : NikeCompletableObserver(compositeDisposable){
                    override fun onComplete() {
                        product.isFavorite = true
                    }
                })
        }
    }
}