package com.bbb.koha.module.registration

import com.bbb.koha.login.RegisterResponseModel
import com.bbb.koha.login.model.ValidateUserRequestModel
import com.bbb.koha.module.registration.model.RegisterUserRequestModel
import com.bbb.koha.network.RetrofitInstance


/**
 *Created by Gaurav Kumar on 7/28/2022
 * QUYTECH
 */
class RegistrationRepository {
    suspend fun registerUser(requestModel: RegisterUserRequestModel) = RetrofitInstance.apiService?.registerUser(requestModel)
    suspend fun getLibraries() = RetrofitInstance.apiService?.getLibraries()
    suspend fun getCategory() = RetrofitInstance.apiService?.getCategory()
}