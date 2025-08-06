package com.example.nus.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class UserSessionViewModel : ViewModel() {
    val userId = mutableStateOf("")
    val isLoggedIn = mutableStateOf(false)
    val showEmotion = mutableStateOf(false)
    
    fun setUserSession(userId: String, showEmotion: Boolean) {
        this.userId.value = userId
        this.showEmotion.value = showEmotion
        this.isLoggedIn.value = true
    }
    
    fun clearSession() {
        userId.value = ""
        showEmotion.value = false
        isLoggedIn.value = false
    }
}
