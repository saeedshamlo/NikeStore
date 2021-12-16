package com.sevenlearn.nike.data.repo.order

import com.sevenlearn.nike.data.Checkout
import com.sevenlearn.nike.data.SubmitOrderResult
import com.sevenlearn.nikestore.data.OrderHistoryItem
import io.reactivex.Single

class OrderRepositoryImpl(val orderDataSource: OrderDataSource):OrderRepository {
    override fun submit(
        firstName: String,
        lastName: String,
        postalCode: String,
        phoneNumber: String,
        address: String,
        paymentMethod: String
    ): Single<SubmitOrderResult> {
        return orderDataSource.submit(firstName,lastName,postalCode,phoneNumber,address,paymentMethod)
    }

    override fun checkOut(orderId: Int): Single<Checkout> {
       return orderDataSource.checkOut(orderId)
    }

    override fun list(): Single<List<OrderHistoryItem>> {
        return orderDataSource.list()
    }
}