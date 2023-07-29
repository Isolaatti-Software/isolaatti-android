package com.isolaatti.profile.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
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

    lateinit var viewBinding: ActivityProfileBinding
    private val viewModel: ProfileViewModel by viewModels()
    private var userId: Int? = null

    private var title = ""

    private val profileObserver = Observer<UserProfileDto> { profile ->
        Picasso.get()
            .load(UrlGen.userProfileImage(profile.id))
            .into(viewBinding.profileImageView)

        title = profile.name
        viewBinding.textViewUsername.text = profile.name
        viewBinding.textViewDescription.text = profile.descriptionText
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        userId = intent.extras?.getInt(EXTRA_USER_ID)
        var scrollRange = -1
        var isShow = false
        viewBinding.topAppBarLayout.addOnOffsetChangedListener(object: OnOffsetChangedListener {


            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) scrollRange = appBarLayout.totalScrollRange
                if(scrollRange + verticalOffset == 0) {
                    viewBinding.collapsingToolbarLayout.title = title
                    isShow = true
                } else if(isShow) {
                    viewBinding.collapsingToolbarLayout.title = " "
                }

            }

        })

        viewBinding.topAppBar.setNavigationOnClickListener {
            finish()
        }


        viewModel.profile.observe(this, profileObserver)

        userId?.let { viewModel.getProfile(it) }

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