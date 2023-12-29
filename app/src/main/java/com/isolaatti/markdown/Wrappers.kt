package com.isolaatti.markdown

object Wrappers {
    val boldWrapper = object: Wrapper {
        override val symbol: String
            get() = "**"
    }

    val italicsWrapper = object: Wrapper {
        override val symbol: String
            get() = "*"
    }
}