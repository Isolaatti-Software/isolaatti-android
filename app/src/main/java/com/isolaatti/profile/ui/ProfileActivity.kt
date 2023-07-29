package com.isolaatti.profile.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.adapter.FragmentViewHolder
import com.google.android.material.tabs.TabLayoutMediator
import com.isolaatti.R
import com.isolaatti.databinding.ActivityProfileBinding
import com.isolaatti.profile.data.remote.UserProfileDto
import com.isolaatti.profile.presentation.ProfileViewModel
import com.isolaatti.utils.UrlGen
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
    private val viewModel: ProfileViewModel by viewModels()

    private val profileObserver = Observer<UserProfileDto> { profile ->
        Picasso.get()
            .load(UrlGen.userProfileImage(profile.id))
            .into(viewBinding.profileImageView)

        viewBinding.textViewUsername.text = profile.name
        viewBinding.textViewDescription.text = profile.descriptionText
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.profileViewPager2.adapter = ViewPagerAdapter(this)
        viewBinding.topAppBar.setNavigationOnClickListener {
            finish()
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

        viewModel.profile.observe(this, profileObserver)
    }

    companion object {
        const val EXTRA_USER_ID = "user_id"

        fun startActivity(context: Context, userId: Int) {
            context.startActivity(Intent(context, ProfileActivity::class.java).apply {
                putExtra(EXTRA_USER_ID, userId)
            })
        }
    }
}