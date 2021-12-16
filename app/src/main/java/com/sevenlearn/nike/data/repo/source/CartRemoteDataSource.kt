package com.sevenlearn.nike.data.repo.source

import com.google.gson.JsonObject
import com.sevenlearn.nike.services.http.ApiService
import com.sevenlearn.nikestore.data.AddToCartResponse
import com.sevenlearn.nikestore.data.CartItemCount
import com.sevenlearn.nikestore.data.CartResponse
import com.sevenlearn.nikestore.data.MessageResponse
import io.reactivex.Single

class CartRemoteDataSource(val apiService: ApiService) : CartDataSource {
    override fun addToCart(productId: Int): Single<AddToCartResponse> {
        return apiService.addToCart(JsonObject().apply {
            addProperty("product_id", productId)
        })
    }

    override fun get(): Single<CartResponse> = apiService.getCart()

    override fun remove(cartItemId: Int): Single<MessageResponse> = apiService.removeItemFromCart(
        JsonObject().apply {
            addProperty("cart_item_id", cartItemId)
        })

    override fun changeCount(cartItemId: Int, count: Int): Single<AddToCartResponse>  =apiService.changeCount(
        JsonObject().apply {
            addProperty("cart_item_id",cartItemId)
            addProperty("count",count)
    })

    override fun getCartItemsCount(): Single<CartItemCount> =apiService.getCartItemsCount()
}