package com.sevenlearn.nike.services

import com.sevenlearn.nike.view.NikeImageView

interface ImageLoadingService {
    fun load(imageView: NikeImageView, imageUrl: String)
}