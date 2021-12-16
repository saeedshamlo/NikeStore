package com.sevenlearn.nike.feature.main

import com.sevenlearn.nike.common.NikeSingleObserver
import com.sevenlearn.nike.common.NikeViewModel
import com.sevenlearn.nike.data.TokenContainer
import com.sevenlearn.nike.data.repo.CartRepository
import com.sevenlearn.nikestore.data.CartItemCount
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus

class MainViewModel(private val cartRepository: CartRepository) : NikeViewModel() {


    fun getCartItemCount() {
        if (!TokenContainer.token.isNullOrEmpty()) {
            cartRepository.getCartItemsCount()
                .subscribeOn(Schedulers.io())
                .subscribe(object : NikeSingleObserver<CartItemCount>(compositeDisposable) {
                    override fun onSuccess(t: CartItemCount) {
                        EventBus.getDefault().postSticky(t)
                    }

                })
        }
    }
}