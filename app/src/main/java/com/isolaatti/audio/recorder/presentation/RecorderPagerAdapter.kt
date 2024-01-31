package com.isolaatti.audio.recorder.presentation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.isolaatti.audio.drafts.ui.AudioDraftsFragment
import com.isolaatti.audio.recorder.ui.AudioRecorderHelperNotesFragment

class RecorderPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AudioRecorderHelperNotesFragment()
            1 -> AudioDraftsFragment()
            else -> Fragment()
        }
    }
}