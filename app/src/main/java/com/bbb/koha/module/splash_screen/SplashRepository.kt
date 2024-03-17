package com.bbb.koha.module.splash_screen

import com.bbb.koha.network.RetrofitInstance

class SplashRepository {
    suspend fun getToken() = RetrofitInstance.apiService?.getToken()
}