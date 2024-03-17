package com.bbb.koha.module.my_account.charges

import com.bbb.koha.login.model.ValidateUserRequestModel
import com.bbb.koha.module.my_account.charges.model.MerchantauthtokenRequest
import com.bbb.koha.module.my_account.charges.model.UserBillDataRequest
import com.bbb.koha.network.RetrofitInstance


/**
 *Created by Gaurav Kumar on 7/28/2022
 * QUYTECH
 */
class ChargesRepository {
    suspend fun getCharges(patronId: Int) = RetrofitInstance.apiService?.getCharges(patronId)
    suspend fun getMerchantAuthToken(url :String, merchantAuthTokenRequest: MerchantauthtokenRequest) = RetrofitInstance.apiService?.getMerchantAuthToken(url,merchantAuthTokenRequest)
    suspend fun getUserBillData(url :String, userBillDataRequest: UserBillDataRequest) = RetrofitInstance.apiService?.getUserBillData(url,userBillDataRequest)
}