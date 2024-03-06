package com.isolaatti.common

import android.content.Context
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.memory.MemoryCache
import com.isolaatti.MyApplication

object CoilImageLoader {
    val imageLoader by lazy {
        ImageLoader
            .Builder(MyApplication.myApp)
            .memoryCache {
                MemoryCache.Builder(MyApplication.myApp.applicationContext)
                    .maxSizePercent(0.25)
                    .build()
            }
            .components {
                add(SvgDecoder.Factory())
            }.build()
    }

    fun getImageLoader(context: Context): ImageLoader {
        return ImageLoader
            .Builder(context)
            .memoryCache {
                MemoryCache.Builder(context)
                    .maxSizePercent(0.25)
                    .build()
            }
            .components {
                add(SvgDecoder.Factory())
            }.build()
    }
}