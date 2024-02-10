package com.isolaatti.markdown

import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.MarkwonConfiguration
import io.noties.markwon.image.destination.ImageDestinationProcessorRelativeToAbsolute

class RelativePathMarkwonPlugin : AbstractMarkwonPlugin() {

    override fun configureConfiguration(builder: MarkwonConfiguration.Builder) {
        builder
            .imageDestinationProcessor(
                ImageDestinationProcessorRelativeToAbsolute
                    .create("https://isolaatti.com/"))
    }
}