package com.sevenlearn.nike.data.repo

import com.sevenlearn.nike.data.Banner
import com.sevenlearn.nike.data.repo.source.BannerDataSource
import io.reactivex.Single

class BannerRepositoryImpl(val bannerRemoteDataSource: BannerDataSource) : BannerRepository {
    override suspend fun getBanners(): List<Banner> = bannerRemoteDataSource.getBanners()
}