package com.isolaatti.images.image_list.presentation

import com.isolaatti.images.common.domain.entity.Image

interface ImageAdapterItem {
    val image: Image?
    val addImage: Boolean

    companion object {
        val AddImagePlaceholder: ImageAdapterItem = object: ImageAdapterItem {
            override val image: Image?
                get() = null

            override val addImage: Boolean
                get() = true
        }

        fun fromImage(image: Image): ImageAdapterItem {
            return object: ImageAdapterItem {
                override val addImage: Boolean
                    get() = false

                override val image: Image?
                    get() = image
            }
        }

    }
}