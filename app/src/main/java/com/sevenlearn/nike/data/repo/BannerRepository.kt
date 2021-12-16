package com.sevenlearn.nike.data.repo

import com.sevenlearn.nike.data.Banner
import io.reactivex.Single

interface BannerRepository {
    suspend fun getBanners():List<Banner>
}