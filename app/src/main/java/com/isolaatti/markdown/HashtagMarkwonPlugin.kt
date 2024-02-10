package com.isolaatti.markdown

import android.text.SpannableString
import android.text.Spanned
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import io.noties.markwon.AbstractMarkwonPlugin

class HashtagMarkwonPlugin : AbstractMarkwonPlugin() {

    override fun beforeSetText(textView: TextView, markdown: Spanned) {
        val matches = "#(\\w|-|_)+".toRegex().findAll(markdown)
        val spannable = SpannableString(markdown)
        matches.forEach {  match ->
            val clickableSpan = object: ClickableSpan() {
                override fun onClick(widget: View) {
                    TODO("Not yet implemented")
                }

            }
            spannable.setSpan(clickableSpan, match.range.first, match.range.last, 1)
        }
    }
}