package com.sevenlearn.nike.services

import com.facebook.drawee.view.SimpleDraweeView
import com.sevenlearn.nike.view.NikeImageView
import java.lang.IllegalStateException

class FrescoImageLoadingService :ImageLoadingService{
    override fun load(imageView: NikeImageView, imageUrl: String) {
        if (imageView is SimpleDraweeView)
            imageView.setImageURI(imageUrl)
        else
            throw IllegalStateException("ImageView must be instance of SimpleDraweeView")
    }
}