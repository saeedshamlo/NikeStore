package com.sevenlearn.nike.data.repo

import com.sevenlearn.nike.data.Product
import com.sevenlearn.nike.data.repo.source.ProductDataSource
import com.sevenlearn.nike.data.repo.source.ProductLocalDataSource
import io.reactivex.Completable
import io.reactivex.Single

class ProductRepositoryImpl(
    val remoteDataSource: ProductDataSource,
    val localDataSource: ProductLocalDataSource
) : ProductRepository {
    override suspend fun getProducts(sort: Int): List<Product> {
        return remoteDataSource.getProducts(sort)
    }


    /*  localDataSource.getFavoriteProducts()
    .flatMap { favorite ->
         remoteDataSource.getProducts(sort).doAfterSuccess{
             val favoriteProductIds = favorite.map {
                 it.id
             }
             it.forEach {product ->
                     if(favoriteProductIds.contains(product.id)){
                         product.isFavorite = true
                     }
             }
         }
     }*/

    override fun getFavoriteProducts(): Single<List<Product>> {
       return localDataSource.getFavoriteProducts()
    }

    override fun addToFavorites(product: Product): Completable {
       return localDataSource.addToFavorites(product)
    }

    override fun deleteFromFavorites(product: Product): Completable {
        return localDataSource.deleteFromFavorites(product)
    }

    override fun searchProduct(query: String): Single<List<Product>>{
        return remoteDataSource.searchProduct(query)
    }


}