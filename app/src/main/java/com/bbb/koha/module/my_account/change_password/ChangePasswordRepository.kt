package com.bbb.koha.module.my_account.change_password

import com.bbb.koha.login.model.ValidateUserRequestModel
import com.bbb.koha.network.RetrofitInstance


/**
 *Created by Gaurav Kumar on 7/28/2022
 * QUYTECH
 */
class ChangePasswordRepository {
    suspend fun resetPassword(patronId: Int?,changePasswordRequest: ChangePasswordRequest) = RetrofitInstance.apiService?.resetPassword(patronId,changePasswordRequest)
}