package com.sevenlearn.nike.feature.order

import androidx.lifecycle.MutableLiveData
import com.sevenlearn.nike.common.NikeSingleObserver
import com.sevenlearn.nike.common.NikeViewModel
import com.sevenlearn.nike.common.asyncNetworkRequest
import com.sevenlearn.nike.data.repo.order.OrderRepository
import com.sevenlearn.nikestore.data.OrderHistoryItem

class OrderViewModel(val orderRepository: OrderRepository): NikeViewModel() {

    val orderLIveData =MutableLiveData<List<OrderHistoryItem>>()
    init {
        progressBarLiveData.value =true
        orderRepository.list()
            .asyncNetworkRequest()
            .doFinally { progressBarLiveData.value = false }
            .subscribe(object :NikeSingleObserver<List<OrderHistoryItem>>(compositeDisposable){
                override fun onSuccess(t: List<OrderHistoryItem>) {
                   orderLIveData.value = t
                }

            })
    }
}