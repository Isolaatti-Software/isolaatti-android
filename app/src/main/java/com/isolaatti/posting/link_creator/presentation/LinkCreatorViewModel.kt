package com.isolaatti.posting.link_creator.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.isolaatti.markdown.Generators

class LinkCreatorViewModel : ViewModel() {
    val liveMarkdown: MutableLiveData<String> = MutableLiveData()
    val markdown: String get() = liveMarkdown.value ?: ""
    val inserted: MutableLiveData<Boolean> = MutableLiveData()

    fun generateMarkdown(text: String, url: String) {
        liveMarkdown.value = Generators.generateLink(text, url)
    }
}