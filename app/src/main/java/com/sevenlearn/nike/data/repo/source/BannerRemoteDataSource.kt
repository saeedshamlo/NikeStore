package com.sevenlearn.nike.data.repo.source

import com.sevenlearn.nike.data.Banner
import com.sevenlearn.nike.services.http.ApiService
import io.reactivex.Single

class BannerRemoteDataSource(val apiService: ApiService) : BannerDataSource {
    override suspend fun getBanners(): List<Banner> = apiService.getBanners()
}