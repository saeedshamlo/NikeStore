package com.sevenlearn.nike.data.repo.order

import com.google.gson.JsonObject
import com.sevenlearn.nike.data.Checkout
import com.sevenlearn.nike.data.SubmitOrderResult
import com.sevenlearn.nike.services.http.ApiService
import com.sevenlearn.nikestore.data.OrderHistoryItem
import io.reactivex.Single

class OrderRemoteDataSource(private val apiService: ApiService):OrderDataSource {
    override fun submit(
        firstName: String,
        lastName: String,
        postalCode: String,
        phoneNumber: String,
        address: String,
        paymentMethod: String
    ): Single<SubmitOrderResult> {
        return apiService.submitOrder(JsonObject().apply {
            addProperty("first_name",firstName)
            addProperty("last_name",lastName)
            addProperty("postal_code",postalCode)
            addProperty("mobile",phoneNumber)
            addProperty("address",address)
            addProperty("payment_method",paymentMethod)

        })
    }

    override fun checkOut(orderId: Int): Single<Checkout> {
      return apiService.checkout(orderId)
    }

    override fun list(): Single<List<OrderHistoryItem>> {
       return apiService.getOrderList()
    }
}