package com.bbb.koha.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationRequest
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.bbb.koha.R
import com.bbb.koha.common.Constant
import com.bbb.koha.common.SharedPreference
import com.bbb.koha.network.RetrofitInstance
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class LocationService : Service() {
    private val NOTIFICATION_CHANNEL_ID = "my_notification_location"
    private val TAG = "LocationService"
    private lateinit var locationRequest: LocationRequest
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var notificationManager: NotificationManager
    private var mPrevLocation = Location("provider")
    private var isRunning = false
    var afterTwenty :Boolean =true
    private lateinit var sharedPreference: SharedPreference

    override fun onCreate() {
        super.onCreate()
        mPrevLocation.latitude = 0.0
        mPrevLocation.longitude = 0.0
        sharedPreference = SharedPreference(this)

        isServiceStarted = true
        val builder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setOngoing(false)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Location Service")
                .setContentText("Location service is running in background")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager: NotificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_ID, NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.description = NOTIFICATION_CHANNEL_ID
            notificationChannel.setSound(null, null)
            notificationManager.createNotificationChannel(notificationChannel)
            startForeground(1, builder.build())
        }
       // registerCallbck()
    }

    fun isRunning(): Boolean {
        return isRunning
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        isRunning=true
        val timer = Timer()
        LocationHelper().startListeningUserLocation(
            this, object : MyLocationListener {
                override fun onLocationChanged(location: Location?) {
                        GlobalScope.async {

                            mLocation = location
                            trackUser()

                        }
                        Log.e(TAG, "onLocationChanged: "+ mLocation?.latitude.toString()+""+mLocation?.longitude.toString())

                }
            })
        Log.e(TAG, "START_NOT_STICKY: "+ mLocation?.latitude.toString()+""+mLocation?.longitude.toString())
        return START_NOT_STICKY
    }

    suspend fun trackUser() {
        val body = mapOf<String, String>(
            "MOBILENO" to sharedPreference.getValueString(Constant.MOBILE)!!,
            "VEHICLENO" to sharedPreference.getValueString(Constant.VEHICLE_NO)!!,
            "LATITUDE" to "${mLocation?.latitude}",
            "LONGITUDE" to "${mLocation?.longitude}",
            "IMEI" to getDeviceIMEIId(this)!!,
            "TRACKTIME" to getDateTime()
        )

        GlobalScope.launch {
            val response = RetrofitInstance.apiService?.trackUser(body)//repository.register(body)
            var result = response?.body()
            Log.d("result","${result?.get(0)?.Activity}")
            if(result?.get(0)?.Activity =="Stop"){
                stopSelf()
            }
        }
    }


    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        isServiceStarted = false
        stopSelf()
    }

     private fun getDateTime(): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm:ss")
        return sdf.format(Date())
    }

    private fun getDeviceIMEIId(context: Context): String? {
        val deviceId: String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Settings.Secure.getString(
                context.contentResolver,
                Settings.Secure.ANDROID_ID
            )
        } else {
            val mTelephony = context.getSystemService(TELEPHONY_SERVICE) as TelephonyManager
            if (mTelephony.deviceId != null) {
                mTelephony.deviceId
            } else {
                Settings.Secure.getString(
                    context.contentResolver,
                    Settings.Secure.ANDROID_ID
                )
            }
        }
        return deviceId
    }

    companion object {
        var mLocation: Location? = null
        var isServiceStarted = false
    }
}