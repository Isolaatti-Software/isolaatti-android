package com.isolaatti.utils

import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator
import io.noties.markwon.image.AsyncDrawable
import io.noties.markwon.image.picasso.PicassoImagesPlugin

object PicassoImagesPluginDef {
    val picassoImagePlugin = PicassoImagesPlugin.create(object: PicassoImagesPlugin.PicassoStore {
        override fun load(drawable: AsyncDrawable): RequestCreator {
            return Picasso.get().load(drawable.destination).tag(drawable)
        }

        override fun cancel(drawable: AsyncDrawable) {
            Picasso.get().cancelTag(drawable)
        }

    })
}