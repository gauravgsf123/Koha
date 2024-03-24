package com.bbb.koha.module.splash_screen

import com.bbb.koha.module.splash_screen.model.RequestModel
import com.bbb.koha.network.RetrofitInstance
import com.bbb.koha.network.RetrofitInstanceForGlobal

class SplashRepository {
    suspend fun getToken() = RetrofitInstance.apiService?.getToken()
    suspend fun getLibraryDetail(requestModel: RequestModel) = RetrofitInstanceForGlobal.apiService?.getLibraryDetail(requestModel)
    suspend fun getLibraryFeature(requestModel: RequestModel) = RetrofitInstanceForGlobal.apiService?.getLibraryFeature(requestModel)

}