package com.bbb.koha.module.splash_screen

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bbb.koha.dashboard.MainRepository
import com.bbb.koha.dashboard.TripSheetResponse
import kotlinx.coroutines.launch
import java.lang.Exception

class SplashViewModel(var app: Application) : ViewModel(){

    var repository = SplashRepository()
    var tokenResponse: MutableLiveData<TokenResponse?> = MutableLiveData()
    fun getToken() {
        viewModelScope.launch() {
            try {
                val response = repository.getToken()
                tokenResponse.value = response?.body()
            } catch (e: Exception) {
            }
        }
    }
}