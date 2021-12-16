package com.sevenlearn.nike.data.repo.source

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.sevenlearn.nike.data.Product
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface ProductLocalDataSource:ProductDataSource {
    override suspend fun getProducts(sort:Int): List<Product>{
        TODO("Not yet implemented")
    }

    @Query("SELECT * FROM PRODUCT")
    override  fun getFavoriteProducts(): Single<List<Product>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override fun addToFavorites(product: Product): Completable

    @Delete
    override fun deleteFromFavorites(product: Product): Completable


    override fun searchProduct(query: String): Single<List<Product>>{
        TODO("Not yet implemented")
    }


}