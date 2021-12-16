package com.sevenlearn.nike.data.repo.source

import com.sevenlearn.nike.data.Banner
import io.reactivex.Single

interface BannerDataSource {
    suspend fun getBanners():List<Banner>
}