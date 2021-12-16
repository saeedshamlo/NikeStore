package com.sevenlearn.nike.data.repo.source

import com.sevenlearn.nike.data.Product
import com.sevenlearn.nike.services.http.ApiService
import io.reactivex.Completable
import io.reactivex.Single

class ProductRemoteDataSource(val apiService: ApiService) :ProductDataSource{
    override suspend fun getProducts(sort:Int): List<Product> = apiService.getProducts(sort.toString())

    override fun getFavoriteProducts(): Single<List<Product>> {
        TODO("Not yet implemented")
    }

    override fun addToFavorites(product: Product): Completable {
        TODO("Not yet implemented")
    }

    override fun deleteFromFavorites(product: Product): Completable {
        TODO("Not yet implemented")
    }

    override fun searchProduct(query: String): Single<List<Product>> {
        return apiService.searchProduct(query)
    }


}