package com.isolaatti.home

import android.os.Bundle
import android.view.Menu
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.isolaatti.R
import com.isolaatti.common.ErrorMessageViewModel
import com.isolaatti.common.IsolaattiBaseActivity
import com.isolaatti.databinding.ActivityHomeBinding
import com.isolaatti.posting.posts.presentation.PostsViewModel
import com.isolaatti.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

class HomeActivity : IsolaattiBaseActivity() {
    private lateinit var viewBinding: ActivityHomeBinding
    private val postsViewModel: PostsViewModel by viewModels()
    override fun onRetry() {

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        viewBinding.bottomNavigation?.setupWithNavController(navHostFragment.navController)
        viewBinding.navigationRail?.setupWithNavController(navHostFragment.navController)

        if(savedInstanceState == null) {
            postsViewModel.getFeed(false)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}