package com.isolaatti.profile.ui

import android.os.Bundle
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.adapter.FragmentViewHolder
import com.google.android.material.tabs.TabLayoutMediator
import com.isolaatti.R
import com.isolaatti.databinding.ActivityProfileBinding
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileActivity : AppCompatActivity() {
    class ViewPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
        override fun getItemCount(): Int = 3

        override fun createFragment(position: Int): Fragment {
            return when(position) {
                0 -> {
                    DiscussionsFragment()
                }
                1 -> {
                    AudiosFragment()
                }
                2 -> {
                    ImagesFragment()
                }
                else -> {Fragment()}
            }
        }

    }


    lateinit var viewBinding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityProfileBinding.inflate(layoutInflater)

        setContentView(viewBinding.root)

        Picasso.get().load("https://isolaatti.com/api/images/image/63a2a6c5270ecc2be2512799?mode=reduced").into(viewBinding.profileImageView)
        viewBinding.textViewUsername.text = "Erik Everardo"
        viewBinding.textViewDescription.text = "Hola"
        viewBinding.profileViewPager2.adapter = ViewPagerAdapter(this)
        viewBinding.topAppBar.setNavigationOnClickListener {
            onBackPressed()
        }

        TabLayoutMediator(viewBinding.profileTabLayout, viewBinding.profileViewPager2) {tab, position ->
            when(position) {
                0 -> {
                    tab.text = getText(R.string.discussions)
                }
                1 -> {
                    tab.text = getText(R.string.audios)
                }
                2 -> {
                    tab.text = getText(R.string.images)
                }
            }
        }.attach()
    }
}