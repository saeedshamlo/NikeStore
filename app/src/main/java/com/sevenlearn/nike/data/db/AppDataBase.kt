package com.sevenlearn.nike.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sevenlearn.nike.data.Product
import com.sevenlearn.nike.data.repo.source.ProductLocalDataSource

@Database(entities = [Product::class],version = 1,exportSchema = false)
abstract class AppDataBase: RoomDatabase() {
    abstract fun productDao():ProductLocalDataSource
}