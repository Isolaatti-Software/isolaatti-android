package com.isolaatti.markdown

object Generators {
    fun generateImage(url: String): String {
        // TODO: normalize input text to avoid conflicts with markdown syntax
        return "![]($url)"
    }


    fun generateLink(text: String, url: String): String {
        // TODO: normalize input text to avoid conflicts with markdown syntax
        return "[${text}]($url)"
    }
}