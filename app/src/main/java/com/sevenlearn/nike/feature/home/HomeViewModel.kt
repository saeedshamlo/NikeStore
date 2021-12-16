package com.sevenlearn.nike.feature.home

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sevenlearn.nike.common.NikeCompletableObserver
import com.sevenlearn.nike.common.NikeSingleObserver
import com.sevenlearn.nike.common.NikeViewModel
import com.sevenlearn.nike.common.asyncNetworkRequest
import com.sevenlearn.nike.data.Banner
import com.sevenlearn.nike.data.Product
import com.sevenlearn.nike.data.SORT_LATEST
import com.sevenlearn.nike.data.SORT_POPULAR
import com.sevenlearn.nike.data.repo.BannerRepository
import com.sevenlearn.nike.data.repo.ProductRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class HomeViewModel(
    val productRepository: ProductRepository,
    bannerRepository: BannerRepository
) :
    NikeViewModel() {
    val productsLastedLiveData = MutableLiveData<List<Product>>()
    val productsPopularLiveData = MutableLiveData<List<Product>>()
    val bannersLiveData = MutableLiveData<List<Banner>>()
    val errorLiveData = MutableLiveData<String>()
    val completionHandlerException = CoroutineExceptionHandler { coroutineContext, throwable ->
        errorLiveData.postValue("خطا نامشخص")
    }

    init {
        viewModelScope.launch(Dispatchers.IO + completionHandlerException) {
            progressBarLiveData.postValue(true)
            productsLastedLiveData.postValue(
                async { productRepository.getProducts(SORT_LATEST) }.await())
            productsPopularLiveData.postValue(
                async { productRepository.getProducts(SORT_POPULAR) }.await())
            bannersLiveData.postValue(async { bannerRepository.getBanners()}.await())
            progressBarLiveData.postValue(false)
        }
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