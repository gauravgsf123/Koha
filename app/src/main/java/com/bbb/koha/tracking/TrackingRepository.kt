package com.bbb.koha.tracking

import com.bbb.koha.network.RetrofitInstance

class TrackingRepository {
    suspend fun trackUser(body:Map<String,String>) = RetrofitInstance.apiService?.trackUser(body)
}