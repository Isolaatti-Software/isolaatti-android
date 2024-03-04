package com.isolaatti.notifications.presentation

import androidx.lifecycle.ViewModel
import com.isolaatti.notifications.domain.NotificationsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(private val notificationsRepository: NotificationsRepository) : ViewModel() {

}