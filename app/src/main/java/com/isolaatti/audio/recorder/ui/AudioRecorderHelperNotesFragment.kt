package com.isolaatti.audio.recorder.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment

class AudioRecorderHelperNotesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return LinearLayout(requireContext()).apply {
            addView(TextView(requireContext()).apply {
                text = "Allow the user to place some info here to be " +
                        "reading while recording. If this screen is launched from comments, show here the post"
            })
        }
    }
}