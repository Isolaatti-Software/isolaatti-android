package com.isolaatti.common

import android.content.Context
import coil.ImageLoader
import coil.decode.SvgDecoder
import com.isolaatti.MyApplication

object CoilImageLoader {
    val imageLoader by lazy {
        ImageLoader
            .Builder(MyApplication.myApp)
            .components {
                add(SvgDecoder.Factory())
            }.build()
    }
}