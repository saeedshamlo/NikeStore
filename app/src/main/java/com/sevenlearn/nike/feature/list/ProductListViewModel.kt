package com.sevenlearn.nike.feature.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sevenlearn.nike.R
import com.sevenlearn.nike.common.NikeCompletableObserver
import com.sevenlearn.nike.common.NikeSingleObserver
import com.sevenlearn.nike.common.NikeViewModel
import com.sevenlearn.nike.common.asyncNetworkRequest
import com.sevenlearn.nike.data.Product
import com.sevenlearn.nike.data.SORT_LATEST
import com.sevenlearn.nike.data.SORT_POPULAR
import com.sevenlearn.nike.data.repo.ProductRepository
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ProductListViewModel(var sort: Int, val productRepository: ProductRepository) :
    NikeViewModel() {

    val errorLiveData = MutableLiveData<String>()
    val completionHandlerException = CoroutineExceptionHandler { coroutineContext, throwable ->
        errorLiveData.postValue("خطا نامشخص")
    }
    val productLiveData = MutableLiveData<List<Product>>()
    val sortTitleLiveData = MutableLiveData<Int>()
    val sortTitles = arrayOf(R.string.sortLasted ,R.string.sortPopular ,
        R.string.sortPriceHighToLow ,R.string.sortPriceLowToHigh,)
    init {
        getProduct()
        sortTitleLiveData.value = sortTitles[sort]
    }

    fun getProduct() {
        viewModelScope.launch(Dispatchers.IO + completionHandlerException) {
            progressBarLiveData.postValue(true)
            productLiveData.postValue( productRepository.getProducts(sort))
            progressBarLiveData.postValue(false)
        }
    }

    fun onSelectedSortByUser(sort: Int){
        this.sort = sort
        this.sortTitleLiveData.value =sortTitles[sort]
        getProduct()
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