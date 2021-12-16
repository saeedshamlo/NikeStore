package com.sevenlearn.nike.data.repo.order

import com.sevenlearn.nike.data.Checkout
import com.sevenlearn.nike.data.SubmitOrderResult
import com.sevenlearn.nikestore.data.OrderHistoryItem
import io.reactivex.Single

interface OrderRepository {

    fun submit(
        firstName: String,
        lastName: String,
        postalCode: String,
        phoneNumber: String,
        address: String,
        paymentMethod:String
    ):Single<SubmitOrderResult>

    fun checkOut(orderId:Int):Single<Checkout>

    fun list():Single<List<OrderHistoryItem>>
}