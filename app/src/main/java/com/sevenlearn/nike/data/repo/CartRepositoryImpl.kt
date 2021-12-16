package com.sevenlearn.nike.data.repo

import com.sevenlearn.nike.data.repo.source.CartDataSource
import com.sevenlearn.nike.data.repo.source.CartRemoteDataSource
import com.sevenlearn.nikestore.data.AddToCartResponse
import com.sevenlearn.nikestore.data.CartItemCount
import com.sevenlearn.nikestore.data.CartResponse
import com.sevenlearn.nikestore.data.MessageResponse
import io.reactivex.Single

class CartRepositoryImpl(val remoteDataSource: CartDataSource):CartRepository {
    override fun addToCart(productId: Int): Single<AddToCartResponse> = remoteDataSource.addToCart(productId)

    override fun get(): Single<CartResponse> = remoteDataSource.get()

    override fun remove(cartItemId: Int): Single<MessageResponse> =remoteDataSource.remove(cartItemId)

    override fun changeCount(cartItemId: Int, count: Int): Single<AddToCartResponse> =remoteDataSource.changeCount(cartItemId,count)

    override fun getCartItemsCount(): Single<CartItemCount> = remoteDataSource.getCartItemsCount()
}