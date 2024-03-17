package com.bbb.koha.reciver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.bbb.koha.service.LocationService

class BootDeviceReceivers : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        //if(PrefManager.read(PrefManager.IS_LOGIN, false)) {
            context?.let {
                ContextCompat.startForegroundService(it, Intent(it, LocationService::class.java))
            }
        /*}else{
            context?.let {
                //ContextCompat.stopService(it, Intent(it, LocationService::class.java))
            }
        }*/
    }
}