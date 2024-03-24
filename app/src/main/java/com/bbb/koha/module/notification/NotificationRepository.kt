package com.bbb.koha.module.notification

import com.bbb.koha.network.RetrofitInstance
import com.bbb.koha.module.my_account.charges.model.MerchantauthtokenRequest
import com.bbb.koha.module.my_account.charges.model.UserBillDataRequest
import com.bbb.koha.module.notification.model.NotificationRequestModel
import com.bbb.koha.network.RetrofitInstanceForGlobal


/**
 *Created by Gaurav Kumar on 7/28/2022
 * QUYTECH
 */
class NotificationRepository {
    suspend fun getNotification(notificationRequestModel: NotificationRequestModel) = RetrofitInstanceForGlobal.apiService?.getNotification(notificationRequestModel)
}