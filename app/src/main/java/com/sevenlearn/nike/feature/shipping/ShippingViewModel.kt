package com.sevenlearn.nike.feature.shipping

import com.sevenlearn.nike.common.NikeViewModel
import com.sevenlearn.nike.data.SubmitOrderResult
import com.sevenlearn.nike.data.repo.order.OrderRepository
import io.reactivex.Single

const val PAYMENT_METHOD_COD ="cash_on_delivery"
const val PAYMENT_METHOD_ONLINE ="online"
class ShippingViewModel(val orderRepository: OrderRepository) : NikeViewModel() {


    fun submitOrder(
        firstName: String,
        lastName: String,
        postalCode: String,
        phoneNumber: String,
        address: String,
        paymentMethod: String):Single<SubmitOrderResult> {
        return orderRepository.submit(firstName,lastName,postalCode,phoneNumber,address,paymentMethod)
    }
}