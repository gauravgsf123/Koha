package com.bbb.koha.payment

import com.bbb.koha.login.model.ValidateUserRequestModel
import com.bbb.koha.network.RetrofitInstance
import com.bbb.koha.module.my_account.charges.model.MerchantauthtokenRequest
import com.bbb.koha.module.my_account.charges.model.UserBillDataRequest


/**
 *Created by Gaurav Kumar on 7/28/2022
 * QUYTECH
 */
class PaymentRepository {
    suspend fun getMerchantAuthToken(url :String, merchantAuthTokenRequest: MerchantauthtokenRequest) = RetrofitInstance.apiService?.getMerchantAuthToken(url,merchantAuthTokenRequest)
    suspend fun getUserBillData(url :String, userBillDataRequest: UserBillDataRequest) = RetrofitInstance.apiService?.getUserBillData(url,userBillDataRequest)
}