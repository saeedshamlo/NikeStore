package com.sevenlearn.nike.feature.checkout

import androidx.lifecycle.MutableLiveData
import com.sevenlearn.nike.common.NikeSingleObserver
import com.sevenlearn.nike.common.NikeViewModel
import com.sevenlearn.nike.common.asyncNetworkRequest
import com.sevenlearn.nike.data.Checkout
import com.sevenlearn.nike.data.repo.order.OrderRepository

class CheckOutViewModel(val orderId:Int,val orderRepository: OrderRepository): NikeViewModel() {

    val checkOutLiveData =MutableLiveData<Checkout>()
    init {
        orderRepository.checkOut(orderId)
            .asyncNetworkRequest()
            .subscribe(object :NikeSingleObserver<Checkout>(compositeDisposable){
                override fun onSuccess(t: Checkout) {
                    checkOutLiveData.value = t
                }

            })
    }
}