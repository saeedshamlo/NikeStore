package com.sevenlearn.nike.data.repo.source

import com.sevenlearn.nike.data.Product
import io.reactivex.Completable
import io.reactivex.Single

interface ProductDataSource {
    suspend fun getProducts(sort:Int): List<Product>

    fun getFavoriteProducts(): Single<List<Product>>

    fun addToFavorites(product: Product): Completable

    fun deleteFromFavorites(product: Product): Completable

    fun searchProduct(query:String):Single<List<Product>>
}