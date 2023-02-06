package com.isolaatti

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.isolaatti.auth.data.AuthRepositoryImpl
import com.isolaatti.home.HomeActivity
import com.isolaatti.login.LogInActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var authRepository: AuthRepositoryImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        var isLoading = true
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition { isLoading }
        super.onCreate(savedInstanceState)
        // Decide what activity to start
        // Set isLoading to false when ended
        val currentToken = authRepository.getCurrentToken()

        if(currentToken == null) {
            startActivity(Intent(this@MainActivity, LogInActivity::class.java))
        } else {
            startActivity(Intent(this@MainActivity, HomeActivity::class.java))
        }
        isLoading = false
    }
}