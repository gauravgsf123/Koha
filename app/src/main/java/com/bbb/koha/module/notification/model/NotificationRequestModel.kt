package com.bbb.koha.module.notification.model

import com.google.gson.annotations.SerializedName

data class NotificationRequestModel(@SerializedName("user_id"          ) var userId        : String?  = null)