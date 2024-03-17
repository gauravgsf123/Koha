package com.bbb.koha.dashboard

import com.bbb.koha.network.RetrofitInstance

class MainRepository {
    suspend fun pickupRequest(body:Map<String,String>): TripSheetResponse? = RetrofitInstance.apiService?.pickupRequest(body)
}