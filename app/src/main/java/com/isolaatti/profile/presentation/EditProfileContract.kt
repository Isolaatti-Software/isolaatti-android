package com.isolaatti.profile.presentation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.activity.result.contract.ActivityResultContract
import com.isolaatti.profile.domain.entity.UserProfile
import com.isolaatti.profile.ui.EditProfileActivity

class EditProfileContract : ActivityResultContract<UserProfile, UserProfile?>() {
    override fun createIntent(context: Context, input: UserProfile): Intent {
        return Intent(context, EditProfileActivity::class.java).apply {
            putExtra(EditProfileActivity.EXTRA_IN_USER_PROFILE, input)
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): UserProfile? {
        if(intent == null || resultCode != Activity.RESULT_OK) return null
        return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            intent.extras?.getSerializable(EditProfileActivity.EXTRA_OUT_USER_PROFILE, UserProfile::class.java)
        else
            intent.extras?.getSerializable(EditProfileActivity.EXTRA_OUT_USER_PROFILE) as UserProfile
    }
}