package com.bbb.koha.module.forgot

import com.bbb.koha.network.RetrofitInstance


/**
 *Created by Gaurav Kumar on 7/28/2022
 * QUYTECH
 */
class ForgotRepository {
    suspend fun getUserDetailByEmail(query: String) = RetrofitInstance.apiService?.getUserDetailByEmail(query)
}